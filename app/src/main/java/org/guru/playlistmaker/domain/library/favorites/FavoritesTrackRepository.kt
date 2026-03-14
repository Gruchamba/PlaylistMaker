package org.guru.playlistmaker.domain.library.favorites

import kotlinx.coroutines.flow.Flow
import org.guru.playlistmaker.domain.search.model.Track

interface FavoritesTrackRepository {

    suspend fun addTrackToFavorites(track: Track)

    suspend fun deleteFromFavorites(track: Track)

    fun getAllFavoriteTracks(): Flow<List<Track>>

}