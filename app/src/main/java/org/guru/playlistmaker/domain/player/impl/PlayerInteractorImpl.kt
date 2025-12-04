package org.guru.playlistmaker.domain.player.impl

import org.guru.playlistmaker.domain.player.PlayerInteractor
import org.guru.playlistmaker.domain.player.PlayerRepository
import org.guru.playlistmaker.domain.player.model.PlayerState

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

    override fun getPlayerState(): PlayerState {
        return playerRepository.getPlayerState()
    }

}