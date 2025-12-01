package org.guru.playlistmaker.data.player.impl

import org.guru.playlistmaker.data.player.PlayerClient
import org.guru.playlistmaker.domain.player.PlayerRepository

class PlayerRepositoryImpl(private val playerClient: PlayerClient) : PlayerRepository {

    override fun preparePlayer(url: String) {
        playerClient.preparePlayer(url)
    }

    override fun startPlayer() {
        playerClient.startPlayer()
    }

    override fun pausePlayer() {
        playerClient.pausePlayer()
    }

    override fun release() {
        playerClient.release()
    }

    override fun getCurrentTimePosition() : Int {
        return playerClient.getCurrentTime()
    }

    override fun getPlayerState(): PlayerClient.PlayerState {
        return playerClient.getState()
    }
}