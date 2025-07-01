package org.guru.playlistmaker

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.guru.playlistmaker.data.Track
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class AudioPlayerActivity : AppCompatActivity() {

    val TAG = AudioPlayerActivity::class.java.name

    private val backBtn: ImageView by lazy { findViewById(R.id.backBtn) }
    private val albumPlaceholder: ImageView by lazy { findViewById(R.id.albumPlaceholder) }
    private val trackName: TextView by lazy { findViewById(R.id.trackName) }
    private val artistName: TextView by lazy { findViewById(R.id.artistName) }
    private val trackDuration: TextView by lazy { findViewById(R.id.trackDuration) }
    private val collectionName: TextView by lazy { findViewById(R.id.trackAlbum) }
    private val releaseDate: TextView by lazy { findViewById(R.id.releaseDate) }
    private val primaryGenreName: TextView by lazy { findViewById(R.id.primaryGenreName) }
    private val country: TextView by lazy { findViewById(R.id.trackCountry) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val track = intent.getSerializableExtra(TRACK_KEY) as Track
        Log.d(TAG, "create audio player: $track")

        track.let {

            Glide.with(this)
                .load(track.getCoverArtwork())
                .placeholder(R.drawable.ic_album_def_img)
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

        backBtn.setOnClickListener{finish()}
    }

    companion object {
        const val TRACK_KEY = "track"
    }
}