package org.guru.playlistmaker.ui.player.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.guru.playlistmaker.domain.player.PlayerInteractor
import org.guru.playlistmaker.domain.player.model.PlayerState
import org.guru.playlistmaker.ui.player.fragment.PlayerViewState
import org.guru.playlistmaker.ui.player.fragment.PlayerViewState.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlayerViewModel(private val url: String) : ViewModel(), KoinComponent {


    private companion object {
        val TAG: String = PlayerViewModel::class.java.name
        const val DELAY = 300L
    }

    private val playerStateLiveData = MutableLiveData<PlayerViewState>()
    fun observePlayerState(): LiveData<PlayerViewState> = playerStateLiveData

    private val playerInteractor: PlayerInteractor by inject()

    private var timerJob: Job? = null

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
        renderState(Prepare())
    }

    private fun startPlayer() {
        renderState(Play())
        playerInteractor.startPlayer()
        startTimerUpdate()
    }

    fun pausePlayer() {
        renderState(Pause(playerInteractor.getCurrentTimePosition()))
        playerInteractor.pausePlayer()
        pauseTimer()
    }

    private fun startTimerUpdate() {
        renderState(Playing(playerInteractor.getCurrentTimePosition()))

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (playerInteractor.getPlayerState() == PlayerState.STATE_PLAYING) {
                delay(DELAY)
                renderState(Playing(playerInteractor.getCurrentTimePosition()))
            }

            if (playerInteractor.getPlayerState() == PlayerState.STATE_PREPARED)
                renderState(Prepare())

        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
    }

    private fun resetTimer() {
        timerJob?.cancel()
        renderState(Playing(0))
    }

}