package org.guru.playlistmaker.data.db.converters

import org.guru.playlistmaker.data.db.entity.TrackEntity
import org.guru.playlistmaker.domain.search.model.Track

class TrackDbConverter {

    fun map(track: Track) : TrackEntity {
        return TrackEntity(
            track.trackId!!,
            track.trackName ?: "",
            track.artistName,
            track.collectionName ?: "",
            track.releaseDate ?: "",
            track.primaryGenreName,
            track.country,
            track.trackTime ?: "",
            track.artworkUrl100,
            track.previewUrl ?: ""
        )
    }

    fun map(trackEntity: TrackEntity) : Track {
        return Track(
            trackEntity.trackId,
            trackEntity.trackName,
            trackEntity.artistName,
            trackEntity.collectionName,
            trackEntity.releaseDate,
            trackEntity.primaryGenreName,
            trackEntity.country,
            trackEntity.trackTimeMillis,
            trackEntity.artworkUrl100,
            trackEntity.previewUrl
        )
    }

}