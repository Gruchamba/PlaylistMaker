package org.guru.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.guru.playlistmaker.data.player.PlayerClient.PlayerState
import org.guru.playlistmaker.domain.player.PlayerInteractor
import org.guru.playlistmaker.ui.player.activity.PlayerViewState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlayerViewModel(private val url: String) : ViewModel(), KoinComponent {

    val TAG = PlayerViewModel::class.java.name

    private val playerStateLiveData = MutableLiveData<PlayerViewState>()
    fun observePlayerState(): LiveData<PlayerViewState> = playerStateLiveData

    private val playerInteractor: PlayerInteractor by inject()

    private val handler = Handler(Looper.getMainLooper())

    private val timerRunnable = Runnable {
        if (playerInteractor.getPlayerState() == PlayerState.STATE_PLAYING) {
            startTimerUpdate()

        } else if (playerInteractor.getPlayerState() == PlayerState.STATE_PREPARED) {
            renderState(PlayerViewState.Prepare())
        }
    }

    init {
        preparePlayer()
    }

    fun release() {
        playerInteractor.release()
        resetTimer()
    }

    override fun onCleared() {
        super.onCleared()
        release()
    }

    fun onPlayButtonClicked() {
        when(playerInteractor.getPlayerState()) {
            PlayerState.STATE_PLAYING -> pausePlayer()
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> startPlayer()
            else -> { Log.e(TAG, "error player state ${playerStateLiveData.value}") }
        }
    }

    private fun renderState(state: PlayerViewState) {
        playerStateLiveData.postValue(state)
    }

    private fun preparePlayer() {
        playerInteractor.preparePlayer(url)
        renderState(PlayerViewState.Prepare())
    }

    private fun startPlayer() {
        Log.i(TAG, "start player")
        renderState(PlayerViewState.Play())
        playerInteractor.startPlayer()
        startTimerUpdate()
    }

    fun pausePlayer() {
        renderState(PlayerViewState.Pause(playerInteractor.getCurrentTimePosition()))
        playerInteractor.pausePlayer()
        pauseTimer()
    }

    private fun startTimerUpdate() {
        renderState(PlayerViewState.Playing(playerInteractor.getCurrentTimePosition()))
        handler.postDelayed(timerRunnable, 200)
    }

    private fun pauseTimer() {
        handler.removeCallbacks(timerRunnable)
    }

    private fun resetTimer() {
        handler.removeCallbacks(timerRunnable)
        renderState(PlayerViewState.Playing(0))
    }
}