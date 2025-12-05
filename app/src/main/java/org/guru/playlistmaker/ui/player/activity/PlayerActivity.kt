package org.guru.playlistmaker.ui.player.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.ActivityPlayerBinding
import org.guru.playlistmaker.domain.search.model.Track
import org.guru.playlistmaker.ui.player.view_model.PlayerViewModel
import org.guru.playlistmaker.ui.util.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.Instant
import java.time.ZoneId

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var track: Track
    private val viewModel: PlayerViewModel by viewModel { parametersOf(track.previewUrl) }

    private var isLike = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = intent.getSerializableExtra(TRACK_KEY) as Track

        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_def_album_img)
            .transform(RoundedCorners(dpToPx(8f, this)))
            .into(binding.albumPlaceholder)

        track.apply {
            trackName?.let { binding.trackName.text = it }
            artistName.let { binding.artistName.text = it }
            trackTime?.let { binding.trackDuration.text = track.trackTime }
            collectionName?.let { binding.trackAlbum.text = it }
            releaseDate?.let {
                val instant = Instant.parse(it)
                val dateTime = instant.atZone(ZoneId.systemDefault())
                binding.releaseDate.text = dateTime.year.toString()
            }
            primaryGenreName.let { binding.primaryGenreName.text = it }
            country.let { binding.trackCountry.text = it }
        }

        viewModel.observePlayerState().observe(this) { it.render(binding) }

        binding.apply {
            backBtn.setOnClickListener { finish() }
            playButton.isEnabled = !track.previewUrl.isNullOrEmpty()
            playButton.setOnClickListener { viewModel.onPlayButtonClicked() }

            likeButton.setOnClickListener {
                isLike = !isLike
                likeButton.setImageResource(
                    if (isLike) R.drawable.ic_not_like_track else R.drawable.ic_like_track
                )
            }
        }


    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onStop() {
        super.onStop()
        viewModel.release()
    }

    companion object {
        const val TRACK_KEY = "track"
    }
}