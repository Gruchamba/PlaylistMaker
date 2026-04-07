package org.guru.playlistmaker.ui.player.addInPlaylistAdapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.ListItemAddTrackInPlaylistBinding
import org.guru.playlistmaker.domain.library.playlist.model.Playlist
import org.guru.playlistmaker.ui.library.newPlaylist.fragment.NewPlaylistFragment.Companion.LOCAL_STORAGE_FOR_IMAGE
import org.guru.playlistmaker.ui.util.dpToPx
import java.io.File

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
            playlistNameView.text = playlist.title
            playlistSize.text = itemView.resources.getQuantityString(
                    R.plurals.tracks_count,
                playlist.tracksIdList.size,
                playlist.tracksIdList.size
            )

            Glide.with(itemView)
                .load(loadImage(playlist.uriImage))
                .placeholder(R.drawable.ic_def_track_img)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(4f, itemView.context)))
                .into(playlistImage)
        }
    }

    private fun loadImage(fileName: String?) : Uri? {
        if (!fileName.isNullOrEmpty()) {
            val file = File(
                itemView.context.getDir(
                    LOCAL_STORAGE_FOR_IMAGE,
                    Context.MODE_PRIVATE),
                fileName
            )

            return if (file.exists()) Uri.fromFile(file) else null
        } else { return null }
    }

}