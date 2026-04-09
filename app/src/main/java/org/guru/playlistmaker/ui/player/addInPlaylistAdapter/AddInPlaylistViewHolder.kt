package org.guru.playlistmaker.ui.player.addInPlaylistAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.ListItemAddTrackInPlaylistBinding
import org.guru.playlistmaker.domain.library.playlist.model.Playlist
import org.guru.playlistmaker.ui.util.dpToPx
import org.guru.playlistmaker.ui.util.loadImageFromLocalStorage

class AddInPlaylistViewHolder(
    private val binding: ListItemAddTrackInPlaylistBinding
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): AddInPlaylistViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemAddTrackInPlaylistBinding.inflate(inflater, parent, false)
            return AddInPlaylistViewHolder(binding)
        }
    }

    fun bind(playlist: Playlist) {

        binding.apply {
            itemPlaylist.playlistNameView.text = playlist.title
            itemPlaylist.playlistSize.text = itemView.resources.getQuantityString(
                    R.plurals.tracks_count,
                playlist.tracksIdList.size,
                playlist.tracksIdList.size
            )

            Glide.with(itemView)
                .load(loadImageFromLocalStorage(itemView.context, playlist.uriImage))
                .placeholder(R.drawable.ic_def_track_img)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(4f, itemView.context)))
                .into(itemPlaylist.playlistImage)
        }
    }

}