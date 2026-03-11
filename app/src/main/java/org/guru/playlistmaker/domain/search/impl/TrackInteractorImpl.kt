package org.guru.playlistmaker.domain.search.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.guru.playlistmaker.domain.search.Resource
import org.guru.playlistmaker.domain.search.TrackInteractor
import org.guru.playlistmaker.domain.search.TrackRepository
import org.guru.playlistmaker.domain.search.model.Track

class TrackInteractorImpl (private val repository: TrackRepository) : TrackInteractor {

    override fun addTrackToHistory(track: Track) {
        repository.addTrackToHistory(track)
    }

    override fun clearTracksHistory() {
        repository.clearTracksHistory()
    }

    override fun readTracksFromHistory(): List<Track> {
        return repository.readTracksFromHistory()
    }

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
        return repository.searchTracks(expression).map { result ->
            when(result) {
                is Resource.Success -> Pair(result.data, null)
                is Resource.Error -> Pair(null, result.message)
            }
        }
    }

}