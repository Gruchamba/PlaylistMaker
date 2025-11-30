package org.guru.playlistmaker.domain.search.model

import java.io.Serializable

data class Track(
    val trackId: String?,
    val trackName: String?,
    val artistName: String,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    val trackTime: String?,
    val artworkUrl100: String,
    val previewUrl: String?
) : Serializable {

    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"312x312bb.jpg")

}