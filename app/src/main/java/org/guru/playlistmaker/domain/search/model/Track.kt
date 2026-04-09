package org.guru.playlistmaker.domain.search.model

import android.icu.text.SimpleDateFormat
import java.io.Serializable
import java.util.Locale

data class Track(
    val trackId: String?,
    val trackName: String?,
    val artistName: String,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    private val trackTime: String?,
    val artworkUrl100: String,
    val previewUrl: String?,
    var isFavorite: Boolean = false
) : Serializable {

    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"312x312bb.jpg")

    fun getTrackTime() = trackTime

    fun getFormatTrackTime(): String {
        return trackTime?.let {
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTime.toLong())
        } ?: ""
    }

}