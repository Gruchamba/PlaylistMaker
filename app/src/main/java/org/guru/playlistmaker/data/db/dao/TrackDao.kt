package org.guru.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.guru.playlistmaker.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToFavorites(trackEntity: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteFromFavorites(trackEntity: TrackEntity)

    @Query("SELECT * FROM favorite_tracks")
    suspend fun getAllFavoriteTracks(): List<TrackEntity>

    @Query("SELECT trackId  FROM favorite_tracks")
    suspend fun getAllFavoriteTracksIds(): List<String>

}