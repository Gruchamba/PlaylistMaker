package org.guru.playlistmaker.data.search.storage

import org.guru.playlistmaker.domain.search.model.Track

interface TracksHistoryStorage {
    fun addTrackToHistory(track: Track)
    fun clearTracksHistory()
    fun readTracksFromHistory() : List<Track>
}