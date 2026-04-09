package org.guru.playlistmaker.ui.library.readPlaylist.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.guru.playlistmaker.databinding.FragmentLibraryReadPlaylistBinding
import org.guru.playlistmaker.domain.library.playlist.model.Playlist
import org.guru.playlistmaker.ui.library.readPlaylist.view_model.ReadPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReadPlaylistFragment : Fragment() {

    private var _binding: FragmentLibraryReadPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReadPlaylistViewModel by viewModel()

    private lateinit var playlist: Playlist

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    companion object {
        const val PLAYLIST_KEY = "playlist"

        fun createArgs(playlist: Playlist) :  Bundle = bundleOf(
            PLAYLIST_KEY to playlist
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

        requireArguments().getSerializable(PLAYLIST_KEY)?.apply {
            playlist = this as Playlist
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.tracksBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> { binding.overlay.visibility = View.GONE }
                    else -> { binding.overlay.visibility = View.VISIBLE }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = when {
                    slideOffset in -1f..0f -> 0f
                    else -> slideOffset + 0.3f
                }

                Log.d("DEBUG","slide $slideOffset alpha ${binding.overlay.alpha}")
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}