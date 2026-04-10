package org.guru.playlistmaker.ui.library.readPlaylist.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import org.guru.playlistmaker.ui.library.newPlaylist.fragment.CreateOrUpdatePlaylistFragment
import org.guru.playlistmaker.ui.library.readPlaylist.view_model.ReadPlaylistViewModel
import org.guru.playlistmaker.ui.player.fragment.PlayerFragment
import org.guru.playlistmaker.ui.search.trackAdapter.TrackAdapter
import org.guru.playlistmaker.ui.util.debounce
import org.guru.playlistmaker.ui.util.dpToPx
import org.guru.playlistmaker.ui.util.loadImageFromLocalStorage
import org.guru.playlistmaker.ui.util.showCustomSnackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Collections

class ReadPlaylistFragment : Fragment() {

    private var _binding: FragmentLibraryReadPlaylistBinding? = null
    private val binding get() = _binding!!

    private var playlistId: Int = 0
    private val viewModel: ReadPlaylistViewModel by viewModel()

    private lateinit var trackBottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var moreBottomSheetBehavior: BottomSheetBehavior<View>

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

        viewModel.observeRemovePlayerState().observe(viewLifecycleOwner) { _ ->
            findNavController().navigateUp()
        }

        binding.apply {

            backBtn.setOnClickListener { findNavController().navigateUp() }

            trackBottomSheetBehavior = BottomSheetBehavior.from(tracksBottomSheet)
            trackBottomSheetBehavior.apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
                peekHeight = dpToPx(10f, requireContext())

                tracksBottomSheet.post {
                    val bottomSheetLocation = IntArray(2)
                    tracksBottomSheet.getLocationOnScreen(bottomSheetLocation)

                    val lastButtonLocation = IntArray(2)
                    moreImg.getLocationOnScreen(lastButtonLocation)

                    val availableSpace = bottomSheetLocation[1] - lastButtonLocation[1] - binding.moreImg.height
                    val minPeek = dpToPx(100f, requireContext())
                    val newPeekHeight = availableSpace.coerceAtLeast(minPeek)

                    peekHeight = newPeekHeight

                    addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

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
                }
            }

            tracksAdapter = TrackAdapter(
                Collections.emptyList(),
                onClick = onTrackClickDebounce,
                onLongClick = { onLongClickOnTrack(it.trackId) }
            )
            tracksRecyclerView.adapter = tracksAdapter

            moreBottomSheetBehavior = BottomSheetBehavior.from(moreBottomSheet)
            moreBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            moreBottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

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

            shareImg.setOnClickListener { onShareClick() }
            moreImg.setOnClickListener { moreBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED }
            shareTxt.setOnClickListener {
                moreBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                onShareClick()
            }
            editPlaylistTxt.setOnClickListener {
                findNavController().navigate(
                    R.id.action_readPlaylistFragment_to_createOrUpdatePlaylistFragment,
                    CreateOrUpdatePlaylistFragment.createArgs(viewModel.getPlaylist())
                )
            }
            removePlaylist.setOnClickListener {
                moreBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                showConfirmCloseDialog(
                    getString(R.string.remove_playlist),
                        getString(R.string.do_you_want_to_delete_a_playlist)
                ) {
                    viewModel.removePlaylist()
                }
            }


        }

        requireArguments().getSerializable(PLAYLIST_ID_KEY)?.apply {
            playlistId = this as Int
            viewModel.setPlaylistId(playlistId)
        }

    }

    private fun onShareClick() {
        if (tracksAdapter.tracks.isEmpty()) {
            showCustomSnackbar(
                layoutInflater,
                binding.root,
                getString(R.string.track_for_share_is_empty)
            )

        } else {
            Intent(Intent.ACTION_SEND).apply {
                putExtra(
                    Intent.EXTRA_TEXT,
                    buildMessageForShare()
                )
                type = "text/plain"
                startActivity(this)
            }
        }
    }

    private fun buildMessageForShare() : String {
        val builder = StringBuilder().append(getString(R.string.playlist))
            .append(" ")
            .append(binding.playlistTitle.text)
            .append("\n")
            .append(getTrackQuantityString(tracksAdapter.tracks.size))
            .append("\n")

        tracksAdapter.tracks.forEachIndexed {
            index, track -> builder.append(index + 1)
                .append(". ")
                .append(track.artistName)
                .append(" - ")
                .append(track.trackName)
                .append(" ")
                .append("(${track.trackTime})")
                .append("\n")
        }
        return builder.toString()
    }

    private fun getTrackQuantityString(count: Int) : String {
         return resources.getQuantityString(
            R.plurals.tracks_count,
            count,
            count
        )
    }

    private fun onLongClickOnTrack(trackId: String?) {
        trackId?.let {
            showConfirmCloseDialog(getString(R.string.remove_track_confirm), null) {
                viewModel.removeTrackFromPlaylist(
                    it,
                    tracksAdapter.tracks
                )
            }
        }
    }

    private fun showConfirmCloseDialog(title: String?, message: String?, onConfirm: () -> Unit) {
        MaterialAlertDialogBuilder(requireActivity(), R.style.AppDialogStyle)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(getString(R.string.no)) { _, _ ->
            }.setPositiveButton(getString(R.string.yes)) { _, _ ->
                onConfirm.invoke()
            }.show()
    }

    private fun renderLoadPlaylist(playlist: Playlist) {
        binding.apply {
            playlist.uriImage?.let {
                Glide.with(this@ReadPlaylistFragment)
                    .load(loadImageFromLocalStorage(requireContext(), playlist.uriImage))
                    .placeholder(R.drawable.ic_def_track_img)
                    .centerCrop()
                    .into(playlistImage)

                Glide.with(this@ReadPlaylistFragment)
                    .load(loadImageFromLocalStorage(requireContext(), playlist.uriImage))
                    .placeholder(R.drawable.ic_def_track_img)
                    .centerCrop()
                    .into(itemPlaylist.playlistImage)
            }

            playlistTitle.text = playlist.title

            if (playlist.description.isNullOrEmpty())
                playlistDescription.visibility = View.GONE
            else playlistDescription.text = playlist.description

            playlistTrackCount.text = getTrackQuantityString(playlist.tracksIdList.size)
            itemPlaylist.playlistNameView.text = playlist.title
            itemPlaylist.playlistSize.text = getTrackQuantityString(playlist.tracksIdList.size)
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
        binding.apply {
            tracksRecyclerView.visibility = View.GONE
            playlistEmptyMessage.visibility = View.VISIBLE

            playlistDuration.text = resources.getQuantityString(
                R.plurals.minutes,
                0,
                0
            )
        }
        tracksAdapter.tracks = emptyList()
        tracksAdapter.notifyDataSetChanged()

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderTracks(list: List<Track>) {
        val totalDuration = list.filter { !it.trackTime.isNullOrEmpty() }
            .map { formatDurationToSeconds(it.trackTime!!) }
            .sumOf { it } / 60

        binding.apply {
            tracksRecyclerView.visibility = View.VISIBLE
            playlistEmptyMessage.visibility = View.GONE
            playlistDuration.text = resources.getQuantityString(
                R.plurals.minutes,
                totalDuration,
                totalDuration
            )
        }

        tracksAdapter.tracks = list
        tracksAdapter.notifyDataSetChanged()

    }

    private fun formatDurationToSeconds(duration: String): Int {
        return duration.split(":")
            .map { it.toInt() }
            .let { (minutes, seconds) -> minutes * 60 + seconds }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}