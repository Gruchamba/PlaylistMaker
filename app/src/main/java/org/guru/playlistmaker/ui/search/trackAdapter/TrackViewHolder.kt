package org.guru.playlistmaker.ui.search.trackAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.ListItemTrackViewBinding
import org.guru.playlistmaker.domain.search.model.Track
import org.guru.playlistmaker.util.dpToPx

class TrackViewHolder(private val binding: ListItemTrackViewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {

        binding.apply {
            Glide.with(itemView)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.ic_def_track_img)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(2f, itemView.context)))
                .into(trackImage)

            track.trackName?.let {
                trackNameView.text = track.trackName
            } ?: "-"

            artistNameView.text = track.artistName
            track.trackTime?.let { durationView.text = track.trackTime }
        }

    }

    companion object {
        fun from(parent: ViewGroup): TrackViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemTrackViewBinding.inflate(inflater, parent, false)
            return TrackViewHolder(binding)
        }
    }

}