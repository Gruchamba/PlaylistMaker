package org.guru.playlistmaker.data

import android.icu.text.SimpleDateFormat
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Locale

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
) : Serializable {

    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")

    fun getFormatTrackTime(): String = SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime?.toLong() ?: "")

}