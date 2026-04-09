package org.guru.playlistmaker.data.db.converters

import org.guru.playlistmaker.data.db.entity.TrackForPlaylistEntity
import org.guru.playlistmaker.domain.search.model.Track

class TrackForPlaylistDbConverter {

    fun map(track: Track) : TrackForPlaylistEntity {
        return TrackForPlaylistEntity(
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

    fun map(trackForPlaylistEntity: TrackForPlaylistEntity) : Track {
        return Track(
            trackForPlaylistEntity.trackId,
            trackForPlaylistEntity.trackName,
            trackForPlaylistEntity.artistName,
            trackForPlaylistEntity.collectionName,
            trackForPlaylistEntity.releaseDate,
            trackForPlaylistEntity.primaryGenreName,
            trackForPlaylistEntity.country,
            trackForPlaylistEntity.trackTimeMillis,
            trackForPlaylistEntity.artworkUrl100,
            trackForPlaylistEntity.previewUrl
        )
    }

}