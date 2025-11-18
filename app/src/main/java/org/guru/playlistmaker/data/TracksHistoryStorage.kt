package org.guru.playlistmaker.data

import org.guru.playlistmaker.domain.models.Track

interface TracksHistoryStorage {
    fun addTrackToHistory(track: Track)
    fun clearTracksHistory()
    fun readTracksFromHistory() : List<Track>
}