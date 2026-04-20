package org.guru.playlistmaker.ui.library.newPlaylist.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.FragmentCreateOrUpdatePlaylistBinding
import org.guru.playlistmaker.domain.library.playlist.model.Playlist
import org.guru.playlistmaker.ui.library.newPlaylist.view_model.CreateOrUpdatePlaylistViewModel
import org.guru.playlistmaker.ui.util.dpToPx
import org.guru.playlistmaker.ui.util.loadImageFromLocalStorage
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class CreateOrUpdatePlaylistFragment : Fragment() {

    private var _binding: FragmentCreateOrUpdatePlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreateOrUpdatePlaylistViewModel by viewModel()

    private var imageUri: String? = null
    private var playlist: Playlist? = null

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let {
                imageUri = saveImage(it)
                Glide.with(this)
                    .load(it)
                    .placeholder(R.drawable.ic_def_track_img)
                    .centerCrop()
                    .transform(RoundedCorners(dpToPx(8f, requireContext())))
                    .into(binding.playlistImage)
            }
        }

    companion object {
        const val LOCAL_STORAGE_FOR_IMAGE = "playlist_images"
        const val PLAYLIST_KEY = "playlist"

        fun createArgs(playlist: Playlist?) :  Bundle = bundleOf(
            PLAYLIST_KEY to playlist
        )
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
        _binding = FragmentCreateOrUpdatePlaylistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleBackNavigation()

        binding.apply {

            requireArguments().getSerializable(PLAYLIST_KEY)?.apply {
                playlist = this as Playlist
            }
            viewModel.setPlaylist(playlist)

            viewModel.observePlaylistState().observe(viewLifecycleOwner) { renderViewState(it) }

            val simpleTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) {
                    confirmBtn.isEnabled = p0.toString().isNotEmpty()
                }
            }

            playlistImage.setOnClickListener {
                pickMedia.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }

            playlistTitle.editText?.addTextChangedListener(simpleTextWatcher)

        }

    }

    private fun renderViewState(state: CreateOrUpdateStateView) {
        when(state) {
            is CreateOrUpdateStateView.UpdateState -> renderUpdateState(state.playlist)
            CreateOrUpdateStateView.CreateState -> renderCreateState()
        }
    }

    private fun renderUpdateState(playlist: Playlist) {
        binding.apply {

            backBtn.setOnClickListener { findNavController().navigateUp() }

            fragmentTitle.text = getString(R.string.edit)
            playlistTitle.editText?.setText(playlist.title)
            playlist.description?.let {
                playlistDescription.editText?.setText(it)
            }

            Glide.with(this@CreateOrUpdatePlaylistFragment)
                .load(loadImageFromLocalStorage(requireContext(),playlist.uriImage))
                .placeholder(R.drawable.ic_def_album_img)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(4f, requireContext())))
                .into(binding.playlistImage)

            confirmBtn.text = getString(R.string.save)
            confirmBtn.setOnClickListener {
                viewModel.updatePlaylist(
                    playlistTitle.editText?.text.toString(),
                    playlistDescription.editText?.text.toString(),
                    imageUri.toString()
                )

                findNavController().navigateUp()
            }
        }
    }

    private fun renderCreateState() {
        binding.apply {

            backBtn.setOnClickListener { backPressed() }

            confirmBtn.text = getString(R.string.create)
            confirmBtn.setOnClickListener {
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
        MaterialAlertDialogBuilder(requireActivity(), R.style.AppDialogStyle)
            .setTitle(getString(R.string.finish_creating_a_playlist))
            .setMessage(getString(R.string.all_unsaved_data_will_be_lost))
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
            }.setPositiveButton(getString(R.string.finish)) { _, _ ->
                findNavController().navigateUp()
            }.show()
    }

}