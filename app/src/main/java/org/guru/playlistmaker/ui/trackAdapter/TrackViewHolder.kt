package org.guru.playlistmaker.ui.trackAdapter

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.guru.playlistmaker.R
import org.guru.playlistmaker.domain.models.Track
import org.guru.playlistmaker.util.dpToPx

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackImage: ImageView
    private val trackNameView: TextView
    private val artistNameView: TextView
    private val durationView: TextView

    private val TAG = "TrackViewHolder"

    init {
        trackImage = itemView.findViewById(R.id.trackImage)
        trackNameView = itemView.findViewById(R.id.trackSongName)
        artistNameView = itemView.findViewById(R.id.trackArtistName)
        durationView = itemView.findViewById(R.id.trackTime)
    }

    fun bind(track: Track) {
        Log.d(TAG, "Create view for track: $track")
        
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