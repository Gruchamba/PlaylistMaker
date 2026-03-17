package org.guru.playlistmaker.domain.search

import kotlinx.coroutines.flow.Flow
import org.guru.playlistmaker.domain.search.model.Track

interface SearchTrackInteractor {

    fun addTrackToHistory(track: Track)

    fun clearTracksHistory()

    fun readTracksFromHistory() : List<Track>

    fun searchTracks(expression: String) : Flow<Pair<List<Track>?, String?>>
}