package org.guru.playlistmaker.domain.api

import org.guru.playlistmaker.domain.models.Track

interface TrackInteractor {

    fun searchTracks(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>)
    }
}