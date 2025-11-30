package org.guru.playlistmaker.domain.search.impl

import org.guru.playlistmaker.domain.search.Resource
import org.guru.playlistmaker.domain.search.TrackInteractor
import org.guru.playlistmaker.domain.search.TrackRepository
import org.guru.playlistmaker.domain.search.model.Track
import java.util.concurrent.Executors

class TrackInteractorImpl (private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun addTrackToHistory(track: Track) {
        repository.addTrackToHistory(track)
    }

    override fun clearTracksHistory() {
        repository.clearTracksHistory()
    }

    override fun readTracksFromHistory(): List<Track> {
        return repository.readTracksFromHistory()
    }

    override fun searchTracks(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            when(val resource = repository.searchTracks(expression)) {
                is Resource.Success -> { consumer.consume(resource.data, null) }
                is Resource.Error -> { consumer.consume(null, resource.message) }
            }
        }
    }
}