package org.guru.playlistmaker.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.guru.playlistmaker.R
import org.guru.playlistmaker.domain.player.PlayerInteractor
import org.guru.playlistmaker.domain.player.model.MusicServiceControl
import org.guru.playlistmaker.domain.player.model.PlayerState
import org.guru.playlistmaker.ui.player.fragment.PlayerViewState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MusicService : Service(), KoinComponent, MusicServiceControl {

    companion object {
        const val ARTIST_NAME_KEY = "artistName"
        const val TRACK_NAME_KEY = "trackName"
        const val PREVIEW_URL_KEY = "previewUrl"

        private const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
        private const val SERVICE_NOTIFICATION_ID = 100

        private const val DELAY = 300L
    }

    private val _playerState = MutableStateFlow<PlayerViewState>(PlayerViewState.Prepare)
    val playerState = _playerState.asStateFlow()

    private val playerInteractor: PlayerInteractor by inject()

    private var artistName: String? = null
    private var trackName: String? = null
    private var previewUrl: String? = null

    private var timerJob: Job? = null

    private val binder = MusicServiceBinder()

    override fun onBind(intent: Intent?): IBinder {

        artistName = intent?.getStringExtra(ARTIST_NAME_KEY)
        trackName = intent?.getStringExtra(TRACK_NAME_KEY)
        previewUrl = intent?.getStringExtra(PREVIEW_URL_KEY)

        preparePlayer()

        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        playerInteractor.release()
        return super.onUnbind(intent)
    }

    /**
     * flags: Int — флаги, указывающие, как именно получен Intent на обработку команды.
     * startId: Int — идентификатор команды, которая запустила сервис.
     */
    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onDestroy() {
        releasePlayer()
        super.onDestroy()
    }

    fun preparePlayer() {
        previewUrl?.let {
            playerInteractor.preparePlayer(it)
            _playerState.value = PlayerViewState.Prepare
        }
    }

    override fun getPlayerViewState(): StateFlow<PlayerViewState> {
        return playerState
    }

    override fun getPlayerState(): PlayerState {
        return playerInteractor.getPlayerState()
    }

    override fun startPlayer() {
        _playerState.value = PlayerViewState.Play
        playerInteractor.startPlayer()
        startTimer()
    }

    override fun pausePlayer() {
        _playerState.value = PlayerViewState.Pause(playerInteractor.getCurrentTimePosition())
        playerInteractor.pausePlayer()
        pauseTimer()
    }

    private fun releasePlayer() {
        playerInteractor.release()
        resetTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (playerInteractor.getPlayerState() == PlayerState.STATE_PLAYING) {
                delay(DELAY)
                _playerState.value = PlayerViewState.Playing(playerInteractor.getCurrentTimePosition())
            }

            if (playerInteractor.getPlayerState() == PlayerState.STATE_PREPARED) {
                _playerState.value = PlayerViewState.Prepare
                removeNotification()
            }

        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
    }

    private fun resetTimer() {
        timerJob?.cancel()
        _playerState.value = PlayerViewState.Playing(0)
    }

    private fun createNotificationChannel() {

        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Music service",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Service for playing music"

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

    }

    override fun addNotification() {
        ServiceCompat.startForeground(
            this,
            SERVICE_NOTIFICATION_ID,
            createServiceNotification(),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
        )
    }

    override fun removeNotification() {
//        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.cancel(SERVICE_NOTIFICATION_ID)
        stopForeground(true)
    }

    private fun createServiceNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("$artistName - $trackName")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()

    }

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }
}