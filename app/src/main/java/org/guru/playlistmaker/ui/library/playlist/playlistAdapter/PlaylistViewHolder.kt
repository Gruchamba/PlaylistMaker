package org.guru.playlistmaker.ui.library.playlist.playlistAdapter

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.guru.playlistmaker.R
import org.guru.playlistmaker.domain.library.playlist.model.Playlist
import org.guru.playlistmaker.ui.library.newPlaylist.fragment.NewPlaylistFragment.Companion.LOCAL_STORAGE_FOR_IMAGE
import java.io.File

class PlaylistViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val image: ImageView = itemView.findViewById(R.id.playlistImage)
    private val title: TextView = itemView.findViewById(R.id.playlistTitle)
    private val playlistSize: TextView = itemView.findViewById(R.id.playlistSize)

    fun bind(playlist: Playlist) {
        title.text = playlist.title
        playlistSize.text = itemView.resources.getQuantityString(
            R.plurals.tracks_count,
            playlist.tracks.size,
            playlist.tracks.size
        )
        loadImage(playlist.uriImage)
    }

    private fun loadImage(fileName: String?) {
        if (!fileName.isNullOrEmpty()) {
            val file = File(
                itemView.context.getDir(
                    LOCAL_STORAGE_FOR_IMAGE,
                    Context.MODE_PRIVATE),
                fileName
            )

            if (file.exists()) {
                val uri = Uri.fromFile(file)
                image.setImageURI(uri)
            } else {
                image.setImageResource(R.drawable.ic_def_track_img)
            }

        } else {
            image.setImageResource(R.drawable.ic_def_track_img)
        }
    }
}