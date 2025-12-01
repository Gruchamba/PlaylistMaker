package org.guru.playlistmaker.domain.player.impl

import org.guru.playlistmaker.data.player.PlayerClient
import org.guru.playlistmaker.domain.player.PlayerInteractor
import org.guru.playlistmaker.domain.player.PlayerRepository

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {

    override fun preparePlayer(url: String) {
        playerRepository.preparePlayer(url)
    }

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun release() {
        playerRepository.release()
    }

    override fun getCurrentTimePosition(): Int {
        return playerRepository.getCurrentTimePosition()
    }

    override fun getPlayerState(): PlayerClient.PlayerState {
        return playerRepository.getPlayerState()
    }

}