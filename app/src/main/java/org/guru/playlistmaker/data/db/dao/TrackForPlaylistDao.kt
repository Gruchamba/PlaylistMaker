package org.guru.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.guru.playlistmaker.data.db.entity.TrackForPlaylistEntity

@Dao
interface TrackForPlaylistDao {

    @Insert(entity = TrackForPlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackForPlaylist(trackForPlaylistEntity: TrackForPlaylistEntity)

    @Query("SELECT * FROM playlist_tracks WHERE trackId = :trackId")
    suspend fun getTracksByIds(trackId: String): TrackForPlaylistEntity

    @Query("SELECT * FROM playlist_tracks WHERE trackId IN (:idList)")
    suspend fun getTracksByIds(idList: List<String>): List<TrackForPlaylistEntity>

    @Query("DELETE FROM playlist_tracks WHERE trackId = :trackId")
    suspend fun deleteTrack(trackId: String)

    @Delete
    suspend fun deleteTrack(track: TrackForPlaylistEntity)


}