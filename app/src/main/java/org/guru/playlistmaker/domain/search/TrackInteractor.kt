package org.guru.playlistmaker.domain.search

import org.guru.playlistmaker.domain.search.model.Track

interface TrackInteractor {

    fun addTrackToHistory(track: Track)

    fun clearTracksHistory()

    fun readTracksFromHistory() : List<Track>

    fun searchTracks(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}