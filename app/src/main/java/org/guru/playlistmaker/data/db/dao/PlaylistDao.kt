package org.guru.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import org.guru.playlistmaker.data.db.entity.PlaylistEntity

@Dao
interface PlaylistDao {

    @Insert(entity = PlaylistEntity::class)
    suspend fun createPlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlists")
    suspend fun getAllPlaylists() : List<PlaylistEntity>

    @Update
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)

}