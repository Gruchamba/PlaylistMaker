package org.guru.playlistmaker.ui.library.libraryPlaylist.fragment

import org.guru.playlistmaker.domain.library.playlist.model.Playlist

sealed interface PlaylistViewState {

    object Empty : PlaylistViewState

    data class Content(val list: List<Playlist>) : PlaylistViewState

}