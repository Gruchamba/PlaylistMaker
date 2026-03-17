package org.guru.playlistmaker.ui.library.favorites.trackAdapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.guru.playlistmaker.domain.search.model.Track

class FavoritesTrackAdapter(var tracks: List<Track>,
                            private val onClick: (Track) -> Unit
) : RecyclerView.Adapter<FavoritesTrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesTrackViewHolder =
        FavoritesTrackViewHolder.from(parent)

    override fun onBindViewHolder(holder: FavoritesTrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener { onClick(track) }
    }

    override fun getItemCount() = tracks.size
}