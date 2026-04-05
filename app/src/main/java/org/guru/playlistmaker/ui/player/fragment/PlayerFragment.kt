package org.guru.playlistmaker.ui.player.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.FragmentPlayerBinding
import org.guru.playlistmaker.domain.search.model.Track
import org.guru.playlistmaker.ui.player.addInPlaylistAdapter.AddInPlaylistAdapter
import org.guru.playlistmaker.ui.player.view_model.PlayerViewModel
import org.guru.playlistmaker.ui.util.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.time.Instant
import java.time.ZoneId

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var track: Track
    private val viewModel: PlayerViewModel by viewModel { parametersOf(track) }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var playlistAdapter: AddInPlaylistAdapter
    private lateinit var onPlaylistClickDebounce: (Track) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(layoutInflater)
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

        viewModel.observePlayerState().observe(viewLifecycleOwner) { it.render(binding) }
        viewModel.observeFavoriteState().observe(viewLifecycleOwner) {
            binding.favoriteBtn.setImageResource(
                if (it) R.drawable.ic_favorite_track else R.drawable.ic_not_favorite_track
            )
        }

        binding.apply {
            backBtn.setOnClickListener { findNavController().navigateUp() }
            playBtn.isEnabled = !track.previewUrl.isNullOrEmpty()
            playBtn.setOnClickListener { viewModel.onPlayButtonClicked() }

            favoriteBtn.setOnClickListener { viewModel.onFavoriteClicked() }

            playlistBtn.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            }

            bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {

                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            overlay.visibility = View.GONE
                        }
                        else -> {
                            overlay.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

        fun createArgs(track: Track) :  Bundle = bundleOf(
            TRACK_KEY to track
        )

    }
}