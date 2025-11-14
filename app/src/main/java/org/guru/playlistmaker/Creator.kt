package org.guru.playlistmaker

import org.guru.playlistmaker.data.TrackRepositoryImpl
import org.guru.playlistmaker.data.network.RetrofitNetworkClient
import org.guru.playlistmaker.domain.api.TrackInteractor
import org.guru.playlistmaker.domain.api.TrackRepository
import org.guru.playlistmaker.domain.impl.TrackInteractorImpl

object Creator {

    private fun getMoviesRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideMoviesInteractor(): TrackInteractor {
        return TrackInteractorImpl(getMoviesRepository())
    }

}