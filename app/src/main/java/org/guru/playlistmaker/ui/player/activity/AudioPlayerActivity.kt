package org.guru.playlistmaker.ui.player.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.ActivityAudioPlayerBinding
import org.guru.playlistmaker.domain.search.model.Track
import org.guru.playlistmaker.ui.player.view_model.PlayerViewModel
import org.guru.playlistmaker.ui.player.view_model.PlayerViewModel.Companion.STATE_PAUSED
import org.guru.playlistmaker.ui.player.view_model.PlayerViewModel.Companion.STATE_PLAYING
import org.guru.playlistmaker.ui.player.view_model.PlayerViewModel.Companion.STATE_PREPARED
import org.guru.playlistmaker.util.dpToPx
import java.time.Instant
import java.time.ZoneId

class AudioPlayerActivity : AppCompatActivity() {

    val TAG = AudioPlayerActivity::class.java.name

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var viewModel: PlayerViewModel

    private var isLike = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = intent.getParcelableExtra<Track>(TRACK_KEY) as Track
        Log.d(TAG, "create audio player: $track")

        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_def_album_img)
            .transform(RoundedCorners(dpToPx(8f, this)))
            .into(binding.albumPlaceholder)

        track.trackName.let { binding.trackName.text = it }
        binding.artistName.text = track.artistName
        track.trackTime.let { binding.trackDuration.text = track.trackTime }
        track.collectionName.let { binding.trackAlbum.text = it }
        track.releaseDate.let {
            val instant = Instant.parse(it)
            val dateTime = instant.atZone(ZoneId.systemDefault())
            binding.releaseDate.text = dateTime.year.toString()
        }
        track.primaryGenreName.let { binding.primaryGenreName.text = it }
        track.country.let { binding.trackCountry.text = it }

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getFactory(track.previewUrl!!)
        )[PlayerViewModel::class.java]

        viewModel.observePlayerState().observe(this) {
            when(it) {
                STATE_PAUSED -> pausePlayer()
                STATE_PLAYING -> startPlayer()
                STATE_PREPARED -> preparePlayer()

            }
        }

        viewModel.observeProgressTime().observe(this) { binding.trackProgress.text = it }

        binding.backBtn.setOnClickListener { finish() }
        binding.playButton.setOnClickListener { viewModel.onPlayButtonClicked() }

        binding.likeButton.setOnClickListener {
            isLike = !isLike
            binding.likeButton.setImageResource(
                if (isLike) R.drawable.ic_not_like_track else R.drawable.ic_like_track
            )
        }

    }

    private fun preparePlayer() {
        binding.playButton.setImageResource(R.drawable.ic_play_btn)
        binding.trackProgress.text = getString(R.string.def_track_progress)
    }

    private fun startPlayer() {
        binding.playButton.setImageResource(R.drawable.ic_stop_btn)
    }

    private fun pausePlayer() {
        binding.playButton.setImageResource(R.drawable.ic_play_btn)
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    companion object {
        const val TRACK_KEY = "track"
    }
}