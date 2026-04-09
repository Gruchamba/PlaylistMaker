package org.guru.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.guru.playlistmaker.data.db.entity.TrackForPlaylistEntity

@Dao
interface TrackForPlaylistDao {

    @Insert(entity = TrackForPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackForPlaylist(trackForPlaylistEntity: TrackForPlaylistEntity)
}