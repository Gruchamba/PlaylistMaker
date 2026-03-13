package org.guru.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks")
class TrackEntity(
    @PrimaryKey
    val trackId: String,
    val trackName: String,
    val artistName: String,
    val collectionName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val releaseDate: String,
    val country: String,
    val primaryGenreName: String,
    val previewUrl: String
)