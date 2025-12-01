package org.guru.playlistmaker.data.player

import android.media.MediaPlayer
import org.guru.playlistmaker.data.player.PlayerClient.PlayerState.STATE_DEFAULT
import org.guru.playlistmaker.data.player.PlayerClient.PlayerState.STATE_PAUSED
import org.guru.playlistmaker.data.player.PlayerClient.PlayerState.STATE_PLAYING
import org.guru.playlistmaker.data.player.PlayerClient.PlayerState.STATE_PREPARED

class MediaPlayerClient : PlayerClient {

    private val mediaPlayer = MediaPlayer()
    private var state = STATE_DEFAULT

    override fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener { state = STATE_PREPARED }
        mediaPlayer.setOnCompletionListener { state = STATE_PREPARED }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        state = STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        state = STATE_PAUSED
    }

    override fun release() {
        mediaPlayer.release()
    }

    override fun getCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }

    override fun getState(): PlayerClient.PlayerState {
        return state
    }

}