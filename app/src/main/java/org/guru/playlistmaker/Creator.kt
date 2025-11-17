package org.guru.playlistmaker

import android.content.SharedPreferences
import org.guru.playlistmaker.data.ConfigurationAppRepository
import org.guru.playlistmaker.data.TrackRepositoryImpl
import org.guru.playlistmaker.data.TracksHistoryStorage
import org.guru.playlistmaker.data.configuration.ConfigurationAppRepositoryImpl
import org.guru.playlistmaker.data.network.RetrofitNetworkClient
import org.guru.playlistmaker.data.storage.TracksHistoryStorageImpl
import org.guru.playlistmaker.domain.api.ConfigurationInteractor
import org.guru.playlistmaker.domain.api.TrackInteractor
import org.guru.playlistmaker.domain.api.TrackRepository
import org.guru.playlistmaker.domain.impl.ConfigurationInteractorImpl
import org.guru.playlistmaker.domain.impl.TrackInteractorImpl

object Creator {

    private fun getTracksRepository(sharedPreferences: SharedPreferences): TrackRepository {
        return TrackRepositoryImpl(getTracksHistoryStorage(sharedPreferences), RetrofitNetworkClient())
    }

    fun provideTracksInteractor(sharedPreferences: SharedPreferences): TrackInteractor {
        return TrackInteractorImpl(getTracksRepository(sharedPreferences))
    }

    private fun getTracksHistoryStorage(sharedPreferences: SharedPreferences) : TracksHistoryStorage {
        return TracksHistoryStorageImpl(sharedPreferences)
    }

    private fun getConfigurationAppRepository(sharedPreferences: SharedPreferences) : ConfigurationAppRepository {
        return ConfigurationAppRepositoryImpl(sharedPreferences)
    }

    fun getConfigurationAppInteractor(sharedPreferences: SharedPreferences) : ConfigurationInteractor {
        return ConfigurationInteractorImpl(getConfigurationAppRepository(sharedPreferences))
    }

}