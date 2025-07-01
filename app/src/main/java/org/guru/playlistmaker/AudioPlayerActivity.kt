package org.guru.playlistmaker

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import org.guru.playlistmaker.data.Track
import java.time.Instant
import java.time.ZoneId

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

    private var isPlay = false
    private var isLike = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val track = intent.getSerializableExtra(TRACK_KEY) as Track
        Log.d(TAG, "create audio player: $track")

        track.let {

            Glide.with(this)
                .load(track.getCoverArtwork())
                .placeholder(R.drawable.ic_def_album_img)
                .centerCrop()
                .into(albumPlaceholder)

            track.trackName.let { trackName.text = it }
            artistName.text = track.artistName
            track.trackTime.let { trackDuration.text = track.getFormatTrackTime() }
            track.collectionName.let { collectionName.text = it }
            track.releaseDate.let {
                val instant = Instant.parse(it)
                val dateTime = instant.atZone(ZoneId.systemDefault())
                releaseDate.text = dateTime.year.toString()
            }
            track.primaryGenreName.let { primaryGenreName.text = it }
            track.country.let { country.text = it }
        }

        backBtn.setOnClickListener { finish() }

        playButton.setOnClickListener {
            isPlay = !isPlay
            playButton.setImageResource(
                if (isPlay) R.drawable.ic_stop_btn else R.drawable.ic_play_btn
            )
        }

        likeButton.setOnClickListener {
            isLike = !isLike
            likeButton.setImageResource(
                if (isLike) R.drawable.ic_not_like_track else R.drawable.ic_like_track
            )
        }

    }

    companion object {
        const val TRACK_KEY = "track"
    }
}