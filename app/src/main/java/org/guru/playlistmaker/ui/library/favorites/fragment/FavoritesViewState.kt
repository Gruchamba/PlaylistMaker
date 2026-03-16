package org.guru.playlistmaker.ui.library.favorites.fragment

import org.guru.playlistmaker.domain.search.model.Track

sealed interface FavoritesViewState {

    object Empty : FavoritesViewState

    data class Content(val list: List<Track>) : FavoritesViewState

}