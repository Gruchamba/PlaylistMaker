package org.guru.playlistmaker.ui.player.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.FragmentPlayerBinding
import org.guru.playlistmaker.domain.search.model.Track
import org.guru.playlistmaker.ui.player.view_model.PlayerViewModel
import org.guru.playlistmaker.ui.util.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.Instant
import java.time.ZoneId
import kotlin.getValue


class PlayerFragment : Fragment() {

    private lateinit var binding: FragmentPlayerBinding
    private lateinit var track: Track
    private val viewModel: PlayerViewModel by viewModel { parametersOf(track.previewUrl) }

    private var isLike = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireArguments().getSerializable(TRACK_KEY)?.apply {
            track = this as Track
        }

        Glide.with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.ic_def_album_img)
            .transform(RoundedCorners(dpToPx(8f, requireContext())))
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

        viewModel.observePlayerState().observe(requireActivity()) { it.render(binding) }

        binding.apply {
            backBtn.setOnClickListener { findNavController().navigateUp() }
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
        val TAG = PlayerFragment::javaClass.name
        const val TRACK_KEY = "track"

        fun createArgs(track: Track) :  Bundle = bundleOf(
            TRACK_KEY to track
        )

    }
}