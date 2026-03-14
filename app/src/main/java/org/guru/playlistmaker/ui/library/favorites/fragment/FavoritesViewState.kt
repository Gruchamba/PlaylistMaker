package org.guru.playlistmaker.ui.library.favorites.fragment

import android.view.View
import org.guru.playlistmaker.databinding.FragmentFavoritesTrackBinding
import org.guru.playlistmaker.domain.search.model.Track

sealed class FavoritesViewState() {

    class Empty : FavoritesViewState() {
        override fun render(binding: FragmentFavoritesTrackBinding) {
            binding.trackRecyclerView.visibility = View.GONE
            binding.trackNotFoundLayout.visibility = View.VISIBLE
        }

    }

    class Content(val list: List<Track>) : FavoritesViewState() {
        override fun render(binding: FragmentFavoritesTrackBinding) {
            binding.trackRecyclerView.visibility = View.VISIBLE
            binding.trackNotFoundLayout.visibility = View.GONE
        }

    }

    abstract fun render(binding: FragmentFavoritesTrackBinding)

}