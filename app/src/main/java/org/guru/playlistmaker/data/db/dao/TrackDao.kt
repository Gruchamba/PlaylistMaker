package org.guru.playlistmaker.data.db.dao

import androidx.room.Dao
import org.guru.playlistmaker.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    suspend fun addTrackToFavorites(trackEntity: TrackEntity)

    suspend fun deleteFromFavorites(trackEntity: TrackEntity)

    suspend fun getAllFavoriteTracks(): List<TrackEntity>

    suspend fun getAllFavoriteTracksIds(): List<String>

}