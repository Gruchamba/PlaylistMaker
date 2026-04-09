package org.guru.playlistmaker.ui.library.readPlaylist.fragment

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.FragmentLibraryReadPlaylistBinding
import org.guru.playlistmaker.domain.library.playlist.model.Playlist
import org.guru.playlistmaker.domain.search.model.Track
import org.guru.playlistmaker.ui.library.readPlaylist.view_model.ReadPlaylistViewModel
import org.guru.playlistmaker.ui.player.fragment.PlayerFragment
import org.guru.playlistmaker.ui.search.trackAdapter.TrackAdapter
import org.guru.playlistmaker.ui.util.debounce
import org.guru.playlistmaker.ui.util.loadImageFromLocalStorage
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Collections
import java.util.Locale

class ReadPlaylistFragment : Fragment() {

    private var _binding: FragmentLibraryReadPlaylistBinding? = null
    private val binding get() = _binding!!

    private var playlistId: Int = 0
    private val viewModel: ReadPlaylistViewModel by viewModel()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private lateinit var tracksAdapter: TrackAdapter
    private lateinit var onTrackClickDebounce: (Track) -> Unit

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val PLAYLIST_ID_KEY = "playlist_id"

        fun createArgs(playlistId: Int) :  Bundle = bundleOf(
            PLAYLIST_ID_KEY to playlistId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryReadPlaylistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onTrackClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false) { track ->
            findNavController().navigate(
                R.id.action_readPlaylistFragment_to_playerFragment,
                PlayerFragment.createArgs(track)
            )
        }

        viewModel.observeTracksState().observe(viewLifecycleOwner) { state ->
            renderLoadTracks(state)
        }

        viewModel.observePlayerState().observe(viewLifecycleOwner) { playlist ->
            renderLoadPlaylist(playlist)
        }

        binding.apply {

            backBtn.setOnClickListener { findNavController().navigateUp() }

            bottomSheetBehavior = BottomSheetBehavior.from(tracksBottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> { overlay.visibility = View.GONE }
                        else -> { overlay.visibility = View.VISIBLE }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    overlay.alpha = when {
                        slideOffset in -1f..0f -> 0f
                        else -> slideOffset + 0.3f
                    }
                }
            })

            tracksAdapter = TrackAdapter(
                Collections.emptyList(),
                onClick = onTrackClickDebounce,
                onLongClick = { onLongClickOnTrack(it.trackId!!) }
            )
            tracksRecyclerView.adapter = tracksAdapter

        }

        requireArguments().getSerializable(PLAYLIST_ID_KEY)?.apply {
            playlistId = this as Int
            viewModel.setPlaylistId(playlistId)
        }

    }

    private fun onLongClickOnTrack(trackId: String) {
        showConfirmCloseDialog {
            viewModel.removeTrackFromPlaylist(
                trackId,
                tracksAdapter.tracks
            )
        }
    }

    private fun showConfirmCloseDialog(onConfirm: () -> Unit) {
        MaterialAlertDialogBuilder(requireActivity(), R.style.AppDialogStyle)
            .setTitle(getString(R.string.remove_track_confirm))
            .setNegativeButton(getString(R.string.no)) { _, _ ->
            }.setPositiveButton(getString(R.string.yes)) { _, _ ->
                onConfirm.invoke()
            }.show()
    }

    private fun renderLoadPlaylist(playlist: Playlist) {
        binding.apply {
            playlist.uriImage?.let { playlistImage.setImageURI(it.toUri())
                Glide.with(this@ReadPlaylistFragment)
                    .load(loadImageFromLocalStorage(requireContext(), playlist.uriImage))
                    .placeholder(R.drawable.ic_def_track_img)
                    .centerCrop()
                    .into(playlistImage)
            }

            playlistTitle.text = playlist.title

            if (playlist.description.isNullOrEmpty())
                playlistDescription.visibility = View.GONE
            else playlistDescription.text = playlist.description

            playlistTrackCount.text = resources.getQuantityString(
                R.plurals.tracks_count,
                playlist.tracksIdList.size,
                playlist.tracksIdList.size
            )
        }
    }

    private fun renderLoadTracks(state: ReadPlaylistFragmentViewState) {
        when(state) {
            ReadPlaylistFragmentViewState.Empty -> renderEmptyTracks()
            is ReadPlaylistFragmentViewState.Content -> renderTracks(state.list)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderEmptyTracks() {
        tracksAdapter.tracks = emptyList()
        tracksAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderTracks(list: List<Track>) {
        val totalDuration = SimpleDateFormat("mm", Locale.getDefault()).format(
            list.filter { !it.getTrackTime().isNullOrEmpty() }
                .sumOf { it.getTrackTime()!!.toInt() }
        ).toInt()

        binding.playlistDuration.text = resources.getQuantityString(
            R.plurals.minutes,
            totalDuration,
            totalDuration
        )

        tracksAdapter.tracks = list
        tracksAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}