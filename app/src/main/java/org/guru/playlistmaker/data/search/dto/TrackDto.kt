package org.guru.playlistmaker.data.search.dto

import android.icu.text.SimpleDateFormat
import com.google.gson.annotations.SerializedName
import java.util.Locale

class TrackDto(
    val trackId: String?,
    val trackName: String?,
    val artistName: String,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    @SerializedName("trackTimeMillis") val trackTime: String?,
    val artworkUrl100: String,
    val previewUrl: String
) {

    fun getFormatTrackTime(): String {
        return trackTime?.let {
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime.toLong())
        } ?: ""
    }

}