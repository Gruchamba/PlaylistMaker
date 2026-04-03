package org.guru.playlistmaker.ui.library.newPlaylist.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import org.guru.playlistmaker.R
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.guru.playlistmaker.databinding.FragmentNewPlaylistBinding
import org.guru.playlistmaker.ui.library.newPlaylist.view_model.NewPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class NewPlaylistFragment : Fragment() {

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewPlaylistViewModel by viewModel()

    private var imageUri: String? = null

    val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                imageUri = saveImage(uri)
                binding.playlistImage.setImageURI(it)
            }
        }

    companion object {
        const val LOCAL_STORAGE_FOR_IMAGE = "playlist_images"
    }

    private fun saveImage(uri: Uri) : String {
        val path = uri.toString()
        val fileName = path.substring(path.lastIndexOf("/") + 1)

        val destinationFile = File(
            requireContext().getDir(LOCAL_STORAGE_FOR_IMAGE, Context.MODE_PRIVATE),
            fileName
        )

        requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
            FileOutputStream(destinationFile).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        return fileName
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBackNavigation()

        binding.apply {

            val simpleTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    createBtn.isEnabled = p0.toString().isNotEmpty()
                }
            }

            backBtn.setOnClickListener { backPressed() }

            playlistImage.setOnClickListener {
                pickMedia.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }

            playlistTitle.editText?.addTextChangedListener(simpleTextWatcher)

            createBtn.setOnClickListener {
                val title = playlistTitle.editText?.text.toString()
                viewModel.createPlaylist(
                    title,
                    playlistDescription.editText?.text.toString(),
                    imageUri.toString()
                )

                Toast.makeText(
                    requireActivity(),
                    "${getString(R.string.playlists)} $title ${getString(R.string.created)} ",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().navigateUp()
            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handleBackNavigation() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun backPressed() {
        if (checkUserData()) showConfirmCloseDialog()
        else findNavController().navigateUp()
    }

    private fun checkUserData() : Boolean {
        val titleIsNotEmpty = binding.playlistTitle.editText?.text?.toString()?.isNotEmpty() ?: false
        val descriptionIsNotEmpty = binding.playlistDescription.editText?.text?.toString()?.isNotEmpty() ?: false

        return imageUri != null || titleIsNotEmpty || descriptionIsNotEmpty
    }

    private fun showConfirmCloseDialog() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(getString(R.string.finish_creating_a_playlist))
            .setMessage(getString(R.string.all_unsaved_data_will_be_lost))
            .setNegativeButton(getString(R.string.no)) { _, _ ->
            }.setPositiveButton(getString(R.string.finish)) { _, _ ->
                findNavController().navigateUp()
            }.show()
    }

}