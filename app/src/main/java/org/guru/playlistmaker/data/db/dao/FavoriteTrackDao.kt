package org.guru.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.guru.playlistmaker.data.db.entity.FavoriteTrackEntity

@Dao
interface FavoriteTrackDao {

    @Insert(entity = FavoriteTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToFavorites(favoriteTrackEntity: FavoriteTrackEntity)

    @Delete(entity = FavoriteTrackEntity::class)
    suspend fun deleteFromFavorites(favoriteTrackEntity: FavoriteTrackEntity)

    @Query("SELECT * FROM favorite_tracks")
    suspend fun getAllFavoriteTracks(): List<FavoriteTrackEntity>

    @Query("SELECT trackId  FROM favorite_tracks")
    suspend fun getAllFavoriteTracksIds(): List<String>

}