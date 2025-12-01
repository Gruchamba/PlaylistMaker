package org.guru.playlistmaker.ui.player.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.ActivityPlayerBinding
import org.guru.playlistmaker.domain.search.model.Track
import org.guru.playlistmaker.ui.player.view_model.PlayerViewModel
import org.guru.playlistmaker.ui.util.dpToPx
import java.time.Instant
import java.time.ZoneId

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerViewModel

    private var isLike = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = intent.getSerializableExtra(TRACK_KEY) as Track

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
            Log.i(TRACK_KEY, "$it")
            it.render(binding)
        }

        binding.backBtn.setOnClickListener { finish() }
        binding.playButton.setOnClickListener { viewModel.onPlayButtonClicked() }

        binding.likeButton.setOnClickListener {
            isLike = !isLike
            binding.likeButton.setImageResource(
                if (isLike) R.drawable.ic_not_like_track else R.drawable.ic_like_track
            )
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    companion object {
        const val TRACK_KEY = "track"
    }
}