package org.guru.playlistmaker.ui.library.libraryPlaylist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.FragmentLibraryPlaylistBinding
import org.guru.playlistmaker.domain.library.playlist.model.Playlist
import org.guru.playlistmaker.ui.library.libraryPlaylist.playlistAdapter.PlaylistAdapter
import org.guru.playlistmaker.ui.library.libraryPlaylist.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private var _binding: FragmentLibraryPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlaylistViewModel by viewModel()

    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryPlaylistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistAdapter = PlaylistAdapter(emptyList())

        binding.apply {
            recyclerView.adapter = playlistAdapter

            newPlaylistBtn.setOnClickListener {
                findNavController().navigate(
                    R.id.action_mediaLibraryFragment_to_newPlaylistFragment
                )
            }
        }

        viewModel.observePlaylistViewState().observe(viewLifecycleOwner) { render(it) }
        viewModel.loadAllPlaylists()

    }

    private fun render(state: PlaylistViewState) {
        when(state) {
            is PlaylistViewState.Content -> showContent(state.list)
            is PlaylistViewState.Empty -> showEmpty()
        }
    }

    private fun showContent(playlists: List<Playlist>) {
        binding.recyclerView.visibility = View.VISIBLE
        binding.playlistNotFoundLayout.visibility = View.GONE
        playlistAdapter.playlists = playlists
        playlistAdapter.notifyDataSetChanged()
    }

    private fun showEmpty() {
        binding.recyclerView.visibility = View.GONE
        binding.playlistNotFoundLayout.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}