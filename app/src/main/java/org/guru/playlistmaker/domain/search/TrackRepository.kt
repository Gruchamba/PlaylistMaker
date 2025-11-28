package org.guru.playlistmaker.domain.search

import org.guru.playlistmaker.domain.search.model.Track
import org.guru.playlistmaker.util.Resource

interface TrackRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
    fun addTrackToHistory(track: Track)
    fun clearTracksHistory()
    fun readTracksFromHistory() : List<Track>
}