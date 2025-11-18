package org.guru.playlistmaker.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.guru.playlistmaker.R
import org.guru.playlistmaker.domain.models.Track
import org.guru.playlistmaker.util.dpToPx
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    val TAG = AudioPlayerActivity::class.java.name

    private val backBtn: ImageView by lazy { findViewById(R.id.backBtn) }
    private val albumPlaceholder: ImageView by lazy { findViewById(R.id.albumPlaceholder) }
    private val trackName: TextView by lazy { findViewById(R.id.trackName) }
    private val artistName: TextView by lazy { findViewById(R.id.artistName) }
    private val addButton: ImageView by lazy { findViewById(R.id.addButton) }
    private val playButton: ImageView by lazy { findViewById(R.id.playButton) }
    private val likeButton: ImageView by lazy { findViewById(R.id.likeButton) }
    private val trackProgress: TextView by lazy { findViewById(R.id.trackProgress) }
    private val trackDuration: TextView by lazy { findViewById(R.id.trackDuration) }
    private val collectionName: TextView by lazy { findViewById(R.id.trackAlbum) }
    private val releaseDate: TextView by lazy { findViewById(R.id.releaseDate) }
    private val primaryGenreName: TextView by lazy { findViewById(R.id.primaryGenreName) }
    private val country: TextView by lazy { findViewById(R.id.trackCountry) }

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    private var mainThreadHandler: Handler? = null

    private var isLike = false

    private val timeUpdate = object : Runnable {
        override fun run() {
            if (playerState == STATE_PLAYING) {

                Log.d(TAG, "Current player position: ${mediaPlayer.currentPosition} ->" +
                        " ${dateFormat.format(mediaPlayer.currentPosition)}")

                trackProgress.text = dateFormat.format(mediaPlayer.currentPosition)
                mainThreadHandler?.postDelayed(this, DELAY)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        mainThreadHandler = Handler(Looper.getMainLooper())

        val track = intent.getParcelableExtra<Track>(TRACK_KEY) as Track
        Log.d(TAG, "create audio player: $track")

        track.let {

            Glide.with(this)
                .load(track.getCoverArtwork())
                .placeholder(R.drawable.ic_def_album_img)
                .transform(RoundedCorners(dpToPx(8f, this)))
                .into(albumPlaceholder)

            track.trackName.let { trackName.text = it }
            artistName.text = track.artistName
            track.trackTime.let { trackDuration.text = track.trackTime }
            track.collectionName.let { collectionName.text = it }
            track.releaseDate.let {
                val instant = Instant.parse(it)
                val dateTime = instant.atZone(ZoneId.systemDefault())
                releaseDate.text = dateTime.year.toString()
            }
            track.primaryGenreName.let { primaryGenreName.text = it }
            track.country.let { country.text = it }
            track.previewUrl?.let { preparePlayer(track.previewUrl) }

        }

        backBtn.setOnClickListener { finish() }
        playButton.setOnClickListener { playbackControl() }

        likeButton.setOnClickListener {
            isLike = !isLike
            likeButton.setImageResource(
                if (isLike) R.drawable.ic_not_like_track else R.drawable.ic_like_track
            )
        }

    }

    private fun preparePlayer(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener { playerState = STATE_PREPARED }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            playButton.setImageResource(R.drawable.ic_play_btn)
            trackProgress.text = getString(R.string.def_track_progress)
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playButton.setImageResource(R.drawable.ic_stop_btn)
        playerState = STATE_PLAYING
        mainThreadHandler?.postDelayed(timeUpdate, DELAY)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.ic_play_btn)
        playerState = STATE_PAUSED
        mainThreadHandler?.removeCallbacks(timeUpdate)
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
        mainThreadHandler?.removeCallbacks(timeUpdate)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainThreadHandler?.removeCallbacksAndMessages(null)
    }

    companion object {

        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3

        const val TRACK_KEY = "track"
        const val DELAY = 300L
    }
}