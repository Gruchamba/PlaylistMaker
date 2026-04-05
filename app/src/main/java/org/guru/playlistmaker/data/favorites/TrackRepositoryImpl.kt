package org.guru.playlistmaker.data.favorites

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.guru.playlistmaker.data.db.converters.TrackDbConverter
import org.guru.playlistmaker.data.db.dao.FavoriteTrackDao
import org.guru.playlistmaker.data.db.entity.FavoriteTrackEntity
import org.guru.playlistmaker.domain.library.favorites.TrackRepository
import org.guru.playlistmaker.domain.search.model.Track

class TrackRepositoryImpl(
    private val favoriteTrackDao: FavoriteTrackDao,
    private val trackDbConvertor: TrackDbConverter
) : TrackRepository {

    override suspend fun addTrackToFavorites(track: Track) {
        favoriteTrackDao.addTrackToFavorites(
            trackDbConvertor.map(track)
        )
    }

    override suspend fun deleteFromFavorites(track: Track) {
        favoriteTrackDao.deleteFromFavorites(
            trackDbConvertor.map(track)
        )
    }

    override fun getAllFavoriteTracks(): Flow<List<Track>> = flow {
        val idList = favoriteTrackDao.getAllFavoriteTracksIds()
        emit(
            convertFromTrackEntity(
                favoriteTrackDao.getAllFavoriteTracks()
            ).map {
                it.isFavorite = idList.contains(it.trackId!!)
                it
            }
        )
    }

    private fun convertFromTrackEntity(tracks: List<FavoriteTrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }

}