package org.guru.playlistmaker.ui.library.libraryPlaylist.playlistAdapter

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.guru.playlistmaker.R
import org.guru.playlistmaker.domain.library.playlist.model.Playlist
import org.guru.playlistmaker.ui.library.newPlaylist.fragment.NewPlaylistFragment.Companion.LOCAL_STORAGE_FOR_IMAGE
import org.guru.playlistmaker.ui.util.dpToPx
import java.io.File

class PlaylistViewHolder(val view: View): RecyclerView.ViewHolder(view) {

    private val image: ImageView = itemView.findViewById(R.id.playlistImage)
    private val title: TextView = itemView.findViewById(R.id.playlistTitle)
    private val playlistSize: TextView = itemView.findViewById(R.id.playlistSize)

    fun bind(playlist: Playlist) {
        title.text = playlist.title
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
            .into(image)
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