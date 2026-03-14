package org.guru.playlistmaker.data.favorites

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.guru.playlistmaker.data.db.AppDatabase
import org.guru.playlistmaker.data.db.converters.TrackDbConverter
import org.guru.playlistmaker.data.db.entity.TrackEntity
import org.guru.playlistmaker.domain.library.favorites.FavoritesTrackRepository
import org.guru.playlistmaker.domain.search.model.Track

class FavoritesTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConverter
) : FavoritesTrackRepository {

    override suspend fun addTrackToFavorites(track: Track) {
        appDatabase.trackDao().addTrackToFavorites(
            trackDbConvertor.map(track)
        )
    }

    override suspend fun deleteFromFavorites(track: Track) {
        appDatabase.trackDao().deleteFromFavorites(
            trackDbConvertor.map(track)
        )
    }

    override fun getAllFavoriteTracks(): Flow<List<Track>> = flow {
        val idList = appDatabase.trackDao().getAllFavoriteTracksIds()
        emit(
            convertFromTrackEntity(
                appDatabase.trackDao().getAllFavoriteTracks()
            ).map {
                it.isFavorite = idList.contains(it.trackId!!)
                it
            }
        )
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }
}