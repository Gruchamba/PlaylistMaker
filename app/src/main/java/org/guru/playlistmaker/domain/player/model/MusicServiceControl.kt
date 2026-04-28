package org.guru.playlistmaker.domain.player.model

import kotlinx.coroutines.flow.StateFlow
import org.guru.playlistmaker.ui.player.fragment.PlayerViewState

interface MusicServiceControl {

    fun getPlayerViewState(): StateFlow<PlayerViewState>
    fun getPlayerState(): PlayerState
    fun startPlayer()
    fun pausePlayer()
    fun addNotification()
    fun removeNotification()

}