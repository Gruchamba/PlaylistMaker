package org.guru.playlistmaker.ui.player.addInPlaylistAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.guru.playlistmaker.domain.library.playlist.model.Playlist
import org.guru.playlistmaker.domain.search.model.Track
import org.guru.playlistmaker.ui.search.trackAdapter.TrackViewHolder

class AddInPlaylistAdapter(
    var playlists: List<Playlist>,
    private val onClick: (Playlist) -> Unit
) : RecyclerView.Adapter<AddInPlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AddInPlaylistViewHolder.from(parent)


    override fun onBindViewHolder(holder: AddInPlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener { onClick(playlist) }
    }

    override fun getItemCount() = playlists.size
}