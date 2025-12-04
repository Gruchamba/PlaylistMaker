package org.guru.playlistmaker.domain.player

import org.guru.playlistmaker.domain.player.model.PlayerState

interface PlayerInteractor {

    fun preparePlayer(url: String)

    fun startPlayer()

    fun pausePlayer()

    fun release()

    fun getCurrentTimePosition() : Int

    fun getPlayerState() : PlayerState

}