package org.guru.playlistmaker.ui.library.readPlaylist.fragment

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.FragmentLibraryReadPlaylistBinding
import org.guru.playlistmaker.ui.library.readPlaylist.view_model.ReadPlaylistViewModel
import org.guru.playlistmaker.ui.util.dpToPx
import org.guru.playlistmaker.ui.util.loadImageFromLocalStorage
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class ReadPlaylistFragment : Fragment() {

    private var _binding: FragmentLibraryReadPlaylistBinding? = null
    private val binding get() = _binding!!

    private var playlistId: Int = 0
    private val viewModel: ReadPlaylistViewModel by viewModel()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    companion object {
        const val PLAYLIST_ID_KEY = "playlist_id"

        fun createArgs(playlistId: Int) :  Bundle = bundleOf(
            PLAYLIST_ID_KEY to playlistId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryReadPlaylistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            viewModel.observePlayerState().observe(viewLifecycleOwner) { playlist ->

                playlist.uriImage?.let { playlistImage.setImageURI(it.toUri())
                    Glide.with(this@ReadPlaylistFragment)
                        .load(loadImageFromLocalStorage(requireContext(), playlist.uriImage))
                        .placeholder(R.drawable.ic_def_track_img)
                        .centerCrop()
                        .into(playlistImage)
                }

                playlistTitle.text = playlist.title

                if (playlist.description.isNullOrEmpty())
                    playlistDescription.visibility = View.GONE
                else playlistDescription.text = playlist.description

                playlistTrackCount.text = resources.getQuantityString(
                    R.plurals.tracks_count,
                    playlist.tracksIdList.size,
                    playlist.tracksIdList.size
                )
            }

            viewModel.observeTracksState().observe(viewLifecycleOwner) { tracks ->

                val totalDuration = SimpleDateFormat("mm", Locale.getDefault()).format(
                    tracks.filter { !it.getTrackTime().isNullOrEmpty() }
                        .sumOf { it.getTrackTime()!!.toInt() }
                ).toInt()

                playlistDuration.text = resources.getQuantityString(
                    R.plurals.minutes,
                    totalDuration,
                    totalDuration
                )

            }

            backBtn.setOnClickListener { findNavController().navigateUp() }

            bottomSheetBehavior = BottomSheetBehavior.from(tracksBottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> { overlay.visibility = View.GONE }
                        else -> { overlay.visibility = View.VISIBLE }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    overlay.alpha = when {
                        slideOffset in -1f..0f -> 0f
                        else -> slideOffset + 0.3f
                    }
                }
            })
        }

        requireArguments().getSerializable(PLAYLIST_ID_KEY)?.apply {
            playlistId = this as Int
            viewModel.setPlaylistId(playlistId)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}