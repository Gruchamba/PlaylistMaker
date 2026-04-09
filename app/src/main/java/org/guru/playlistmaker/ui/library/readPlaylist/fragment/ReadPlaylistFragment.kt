package org.guru.playlistmaker.ui.library.readPlaylist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import org.guru.playlistmaker.databinding.FragmentLibraryReadPlaylistBinding
import org.guru.playlistmaker.domain.library.playlist.model.Playlist
import org.guru.playlistmaker.ui.library.readPlaylist.view_model.ReadPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReadPlaylistFragment : Fragment() {

    private var _binding: FragmentLibraryReadPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReadPlaylistViewModel by viewModel()

    private lateinit var playlist: Playlist

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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}