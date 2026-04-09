package org.guru.playlistmaker.data.db.converters

import org.guru.playlistmaker.data.db.entity.FavoriteTrackEntity
import org.guru.playlistmaker.domain.search.model.Track

class TrackDbConverter {

    fun map(track: Track) : FavoriteTrackEntity {
        return FavoriteTrackEntity(
            track.trackId!!,
            track.trackName ?: "",
            track.artistName,
            track.collectionName ?: "",
            track.releaseDate ?: "",
            track.primaryGenreName,
            track.country,
            track.getTrackTime() ?: "",
            track.artworkUrl100,
            track.previewUrl ?: ""
        )
    }

    fun map(favoriteTrackEntity: FavoriteTrackEntity) : Track {
        return Track(
            favoriteTrackEntity.trackId,
            favoriteTrackEntity.trackName,
            favoriteTrackEntity.artistName,
            favoriteTrackEntity.collectionName,
            favoriteTrackEntity.releaseDate,
            favoriteTrackEntity.primaryGenreName,
            favoriteTrackEntity.country,
            favoriteTrackEntity.trackTimeMillis,
            favoriteTrackEntity.artworkUrl100,
            favoriteTrackEntity.previewUrl
        )
    }

}