package org.guru.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.guru.playlistmaker.data.db.entity.TrackForPlaylistEntity
import org.guru.playlistmaker.domain.search.model.Track

@Dao
interface TrackForPlaylistDao {

    @Insert(entity = TrackForPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackForPlaylist(trackForPlaylistEntity: TrackForPlaylistEntity)

    @Query("SELECT * FROM playlist_tracks WHERE trackId IN (:idList)")
    suspend fun getTracksByIds(idList: List<String>): List<TrackForPlaylistEntity>
}