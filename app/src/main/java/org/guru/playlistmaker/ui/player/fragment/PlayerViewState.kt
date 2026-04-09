package org.guru.playlistmaker.ui.player.fragment

import org.guru.playlistmaker.domain.library.playlist.model.Playlist

sealed interface PlayerViewState {

    object Play : PlayerViewState

    data class Playing(val playerPosition: Int) : PlayerViewState

    data class Pause(val playerPosition: Int) : PlayerViewState

    object Prepare : PlayerViewState

    data class LoadPlaylists(val list: List<Playlist>) : PlayerViewState

}