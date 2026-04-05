package org.guru.playlistmaker.ui.player.fragment

sealed interface AddInPlaylistState {

    data class AlreadyExist(val playlistTitle: String) : AddInPlaylistState

    data class Successful(val playlistTitle: String) : AddInPlaylistState
}