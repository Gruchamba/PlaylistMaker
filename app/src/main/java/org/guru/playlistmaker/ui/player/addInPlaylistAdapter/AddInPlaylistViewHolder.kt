package org.guru.playlistmaker.ui.player.addInPlaylistAdapter

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.ListItemAddTrackInPlaylistBinding
import org.guru.playlistmaker.domain.library.playlist.model.Playlist
import org.guru.playlistmaker.ui.library.newPlaylist.fragment.NewPlaylistFragment.Companion.LOCAL_STORAGE_FOR_IMAGE
import java.io.File

class AddInPlaylistViewHolder(
    private val binding: ListItemAddTrackInPlaylistBinding,
    private val resources: Resources
) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): AddInPlaylistViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemAddTrackInPlaylistBinding.inflate(inflater, parent, false)
            return AddInPlaylistViewHolder(binding, parent.resources)
        }
    }

    fun bind(playlist: Playlist) {
        binding.apply {
            playlistNameView.text = playlist.title
            playlistSize.text = resources.getQuantityString(
                    R.plurals.tracks_count,
            playlist.tracks.size,
            playlist.tracks.size
            )

            val existUri = loadImage(playlist.uriImage)
            if (existUri != null) playlistImage.setImageURI(existUri)
            else playlistImage.setImageResource(R.drawable.ic_def_track_img)
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

            if (file.exists()) {
                val uri = Uri.fromFile(file)
                return uri
            } else {
                return null
            }

        } else {
            return null
        }
    }

}