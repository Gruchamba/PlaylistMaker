package org.guru.playlistmaker.data.player

interface PlayerClient {

    fun preparePlayer(url: String)

    fun startPlayer()

    fun pausePlayer()

    fun release()

    fun getCurrentTime() : Int

    fun getState() : PlayerState

    enum class  PlayerState {
        STATE_DEFAULT,
        STATE_PREPARED,
        STATE_PLAYING,
        STATE_PAUSED
    }

}