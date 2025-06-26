package org.guru.playlistmaker.trackAdapter

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.guru.playlistmaker.R
import org.guru.playlistmaker.data.Track
import java.util.Locale

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
            .placeholder(R.drawable.ic_track_def_img)
            .transform(RoundedCorners(dpToPx(2f, itemView.context)))
            .centerCrop()
            .into(trackImage)

        track.trackName?.let {
            trackNameView.text = track.trackName
        } ?: "-"

        artistNameView.text = track.artistName

        track.trackTime?.let {
            durationView.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime.toLong())
        } ?: "-"

    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics).toInt()
    }
}