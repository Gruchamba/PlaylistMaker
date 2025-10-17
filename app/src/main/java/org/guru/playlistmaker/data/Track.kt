package org.guru.playlistmaker.data

import android.icu.text.SimpleDateFormat
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Parcelize
data class Track(
    val trackId: String,
    val trackName: String?,
    val artistName: String,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    @SerializedName("trackTimeMillis") val trackTime: String?,
    val artworkUrl100: String
) : Parcelable {

    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"312x312bb.jpg")

    fun getFormatTrackTime(): String {
        return trackTime?.let {
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime.toLong())
        } ?: ""
    }

}