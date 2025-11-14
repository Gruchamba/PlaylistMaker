package org.guru.playlistmaker.domain.impl

import org.guru.playlistmaker.domain.api.TrackInteractor
import org.guru.playlistmaker.domain.api.TrackRepository
import java.util.concurrent.Executors

class TrackInteractorImpl (private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.searchMovies(expression))
        }
    }
}