package org.guru.playlistmaker.domain.player

import org.guru.playlistmaker.data.player.PlayerClient

interface PlayerInteractor {

    fun preparePlayer(url: String)

    fun startPlayer()

    fun pausePlayer()

    fun release()

    fun getCurrentTimePosition() : Int

    fun getPlayerState() : PlayerClient.PlayerState

}