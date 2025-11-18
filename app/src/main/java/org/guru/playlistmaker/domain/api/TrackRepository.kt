package org.guru.playlistmaker.domain.api

import org.guru.playlistmaker.domain.models.Track

interface TrackRepository {
    fun searchTracks(expression: String): List<Track>
    fun addTrackToHistory(track: Track)
    fun clearTracksHistory()
    fun readTracksFromHistory() : List<Track>
}