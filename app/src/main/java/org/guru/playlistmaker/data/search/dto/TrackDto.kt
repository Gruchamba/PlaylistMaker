package org.guru.playlistmaker.data.search.dto

import com.google.gson.annotations.SerializedName

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
)