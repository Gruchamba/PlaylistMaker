package org.guru.playlistmaker.data.db.converters

import org.guru.playlistmaker.data.db.entity.TrackEntity
import org.guru.playlistmaker.domain.search.model.Track

class TrackDbConverter {

    fun map(track: Track) : TrackEntity {
        return TrackEntity(
            track.trackId!!,
            track.artistName,
            track.trackName ?: "",
            track.collectionName ?: "",
            track.trackTime ?.toLong() ?: 0L,
            track.artworkUrl100,
            track.releaseDate ?: "",
            track.country,
            track.primaryGenreName,
            track.artworkUrl100
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
            trackEntity.trackTimeMillis.toString(),
            trackEntity.artworkUrl100,
            trackEntity.previewUrl
        )
    }

}