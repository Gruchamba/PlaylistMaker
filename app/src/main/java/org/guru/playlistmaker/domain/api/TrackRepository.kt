package org.guru.playlistmaker.domain.api

import org.guru.playlistmaker.domain.models.Track
import org.guru.playlistmaker.util.Resource

interface TrackRepository {
    fun searchTracks(expression: String): Resource<List<Track>>
    fun addTrackToHistory(track: Track)
    fun clearTracksHistory()
    fun readTracksFromHistory() : List<Track>
}