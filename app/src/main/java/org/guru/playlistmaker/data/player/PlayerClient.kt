package org.guru.playlistmaker.data.player

import org.guru.playlistmaker.domain.player.model.PlayerState

interface PlayerClient {

    fun preparePlayer(url: String)

    fun startPlayer()

    fun pausePlayer()

    fun release()

    fun getCurrentTime() : Int

    fun getState() : PlayerState

}