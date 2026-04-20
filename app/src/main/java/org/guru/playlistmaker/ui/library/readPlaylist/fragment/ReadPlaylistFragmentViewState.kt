package org.guru.playlistmaker.ui.library.readPlaylist.fragment

import org.guru.playlistmaker.domain.search.model.Track

sealed interface ReadPlaylistFragmentViewState {

    data object Empty : ReadPlaylistFragmentViewState

    data class Content(val list: List<Track>) : ReadPlaylistFragmentViewState

}