package org.guru.playlistmaker.ui.player.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.FragmentPlayerBinding
import org.guru.playlistmaker.domain.library.playlist.model.Playlist
import org.guru.playlistmaker.domain.search.model.Track
import org.guru.playlistmaker.ui.library.newPlaylist.fragment.CreateOrUpdatePlaylistFragment
import org.guru.playlistmaker.ui.player.addInPlaylistAdapter.AddInPlaylistAdapter
import org.guru.playlistmaker.ui.player.view_model.PlayerViewModel
import org.guru.playlistmaker.ui.util.debounce
import org.guru.playlistmaker.ui.util.dpToPx
import org.guru.playlistmaker.ui.util.showCustomSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.util.Collections
import java.util.Locale

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var track: Track
    private val viewModel: PlayerViewModel by viewModel { parametersOf(track) }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var playlistAdapter: AddInPlaylistAdapter
    private lateinit var onPlaylistClickDebounce: (Playlist) -> Unit

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

        viewModel.observePlayerState().observe(viewLifecycleOwner) { renderPlayerState(it) }

        viewModel.observeFavoriteState().observe(viewLifecycleOwner) {
            binding.favoriteBtn.setImageResource(
                if (it) R.drawable.ic_favorite_track else R.drawable.ic_not_favorite_track
            )
        }

        viewModel.observeAddInPlaylistState().observe(viewLifecycleOwner) {
            renderAddInPlaylistResult(it)
        }

        binding.apply {
            backBtn.setOnClickListener { findNavController().navigateUp() }
            playBtn.isEnabled = !track.previewUrl.isNullOrEmpty()
            playBtn.setOnClickListener { viewModel.onPlayButtonClicked() }

            favoriteBtn.setOnClickListener { viewModel.onFavoriteClicked() }

            playlistBtn.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                viewModel.loadPlaylists()
            }

            createNewPlaylistBtn.setOnClickListener {
                findNavController().navigate(
                    R.id.action_playerFragment_to_createOrUpdatePlaylistFragment,
                    CreateOrUpdatePlaylistFragment.createArgs(null)
                )
            }

            bottomSheetBehavior = BottomSheetBehavior.from(binding.playlistsBottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> { overlay.visibility = View.GONE }
                        else -> { overlay.visibility = View.VISIBLE }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    overlay.alpha = when {
                        slideOffset in -1f..0f -> slideOffset + 1f
                        else -> 1f
                    }
                }
            })

            onPlaylistClickDebounce = debounce(
                CLICK_DEBOUNCE_DELAY,
                viewLifecycleOwner.lifecycleScope,
                false) { playlist ->

                viewModel.addTrackInPlaylist(playlist, track)

            }

            playlistAdapter = AddInPlaylistAdapter(
                Collections.emptyList(),
                onPlaylistClickDebounce
            )

            playlistRecyclerView.adapter = playlistAdapter

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

    private fun renderAddInPlaylistResult(state: AddInPlaylistState) {
        when(state) {
            is AddInPlaylistState.AlreadyExist -> { renderAddInPlaylistAlreadyExistState(state.playlistTitle)}
            is AddInPlaylistState.Successful -> { renderAddedInPlaylistState(state.playlistTitle) }
        }
    }

    private fun renderAddInPlaylistAlreadyExistState(title: String) {
        showCustomSnackbar(
            layoutInflater,
            binding.root,
            "${getString(R.string.track_already_exist_in_playlist)} $title"
        )
    }

    private fun renderAddedInPlaylistState(title: String) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        showCustomSnackbar(
            layoutInflater,
            binding.root,
            "${getString(R.string.added_to_playlist)} $title"
        )
    }

    private fun renderPlayerState(state: PlayerViewState) {
        when(state) {
            is PlayerViewState.Pause -> renderPauseState(state.playerPosition)
            is PlayerViewState.Play -> renderPlayState()
            is PlayerViewState.Playing -> renderPlayingState(state.playerPosition)
            is PlayerViewState.Prepare -> renderPrepareState()
            is PlayerViewState.LoadPlaylists -> renderLoadPlaylistsState(state.list)
        }
    }

    private fun renderPlayState() {
        binding.apply {
            playBtn.setImageResource(R.drawable.ic_stop_btn)
            trackProgress.text = simpleDateFormat.format(0)
        }
    }

    private fun renderPlayingState(playerPosition: Int) {
        binding.apply {
            playBtn.setImageResource(R.drawable.ic_stop_btn)
            trackProgress.text = simpleDateFormat.format(playerPosition)
        }
    }

    private fun renderPauseState(playerPosition: Int) {
        binding.apply {
            playBtn.setImageResource(R.drawable.ic_play_btn)
            trackProgress.text = simpleDateFormat.format(playerPosition)
        }
    }

    private fun renderPrepareState() {
        binding.apply {
            playBtn.setImageResource(R.drawable.ic_play_btn)
            binding.trackProgress.text = ContextCompat.getString(
                requireActivity(),
                R.string.def_track_progress
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderLoadPlaylistsState(list: List<Playlist>) {
        playlistAdapter.playlists = list
        playlistAdapter.notifyDataSetChanged()
    }

    companion object {
        private val simpleDateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val TRACK_KEY = "track"

        fun createArgs(track: Track) :  Bundle = bundleOf(
            TRACK_KEY to track
        )
    }
}