package org.guru.playlistmaker.ui.player.fragment

sealed interface PlayerViewState {


    object Play : PlayerViewState

    data class Playing(val playerPosition: Int) : PlayerViewState


    data class Pause(val playerPosition: Int) : PlayerViewState

    object Prepare : PlayerViewState

}