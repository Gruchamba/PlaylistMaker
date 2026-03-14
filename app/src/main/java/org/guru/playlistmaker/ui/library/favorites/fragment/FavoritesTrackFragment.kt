package org.guru.playlistmaker.ui.library.favorites.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.FragmentFavoritesTrackBinding
import org.guru.playlistmaker.domain.search.model.Track
import org.guru.playlistmaker.ui.library.favorites.trackAdapter.FavoritesTrackAdapter
import org.guru.playlistmaker.ui.library.favorites.view_model.FavoritesTrackViewModel
import org.guru.playlistmaker.ui.player.fragment.PlayerFragment
import org.guru.playlistmaker.ui.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Collections

class FavoritesTrackFragment : Fragment() {

    private var _binding: FragmentFavoritesTrackBinding? = null
    private val binding get() = _binding!!

    private lateinit var tracksAdapter: FavoritesTrackAdapter
    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val viewModel: FavoritesTrackViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesTrackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false) { track ->
            findNavController().navigate(
                R.id.action_mediaLibraryFragment_to_playerFragment,
                PlayerFragment.createArgs(track)
            )
        }

        tracksAdapter = FavoritesTrackAdapter(
            Collections.emptyList(),
            onClick = onTrackClickDebounce
        )

        binding.trackRecyclerView.adapter = tracksAdapter

        viewModel.observeFavoritesState().observe(viewLifecycleOwner) { render(it) }
        viewModel.loadFavoritesTrack()
    }

    private fun render(state: FavoritesViewState) {

        state.render(binding)

        if (state is FavoritesViewState.Content) {
            tracksAdapter.tracks = state.list
            tracksAdapter.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}