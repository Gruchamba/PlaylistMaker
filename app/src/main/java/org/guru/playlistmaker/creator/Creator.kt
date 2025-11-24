package org.guru.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import org.guru.playlistmaker.domain.api.ConfigurationAppRepository
import org.guru.playlistmaker.data.TrackRepositoryImpl
import org.guru.playlistmaker.data.TracksHistoryStorage
import org.guru.playlistmaker.data.configuration.ConfigurationAppRepositoryImpl
import org.guru.playlistmaker.data.configuration.ConfigurationAppRepositoryImpl.Companion.APP_PREFERENCES
import org.guru.playlistmaker.data.network.RetrofitNetworkClient
import org.guru.playlistmaker.data.storage.TracksHistoryStorageImpl
import org.guru.playlistmaker.domain.api.ConfigurationInteractor
import org.guru.playlistmaker.domain.api.TrackInteractor
import org.guru.playlistmaker.domain.api.TrackRepository
import org.guru.playlistmaker.domain.impl.ConfigurationInteractorImpl
import org.guru.playlistmaker.domain.impl.TrackInteractorImpl

object Creator {

    private lateinit var preferences: SharedPreferences

    fun initialize(appContext: Context) {
        preferences = appContext.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        getConfigurationAppRepository().initializeTheme()
    }

    private fun getTracksRepository(): TrackRepository {
        return TrackRepositoryImpl(getTracksHistoryStorage(), RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTracksRepository())
    }

    private fun getTracksHistoryStorage() : TracksHistoryStorage {
        return TracksHistoryStorageImpl(preferences)
    }

    private fun getConfigurationAppRepository() : ConfigurationAppRepository {
        return ConfigurationAppRepositoryImpl(preferences)
    }

    fun getConfigurationAppInteractor() : ConfigurationInteractor {
        return ConfigurationInteractorImpl(getConfigurationAppRepository())
    }

}