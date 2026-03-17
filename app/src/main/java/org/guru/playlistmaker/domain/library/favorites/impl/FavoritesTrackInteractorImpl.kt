package org.guru.playlistmaker.domain.library.favorites.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.guru.playlistmaker.domain.library.favorites.FavoritesTrackInteractor
import org.guru.playlistmaker.domain.library.favorites.FavoritesTrackRepository
import org.guru.playlistmaker.domain.search.model.Track

class FavoritesTrackInteractorImpl(
    private val favoritesRepository: FavoritesTrackRepository
) : FavoritesTrackInteractor {

    override suspend fun addTrackToFavorites(track: Track) {
        favoritesRepository.addTrackToFavorites(track)
    }

    override suspend fun deleteFromFavorites(track: Track) {
        favoritesRepository.deleteFromFavorites(track)
    }

    override fun getAllFavoriteTracks(): Flow<List<Track>> {
        return favoritesRepository.getAllFavoriteTracks().map { tracks ->
            tracks.sortedBy { it.isFavorite }
        }
    }
}