package org.guru.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Int = 0,
    val title: String,
    val description: String,
    val uriImage: String,
    val tracks: String,
    val size: Int
)