package org.guru.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import org.guru.playlistmaker.data.search.RetrofitNetworkClient
import org.guru.playlistmaker.data.search.impl.TrackRepositoryImpl
import org.guru.playlistmaker.data.search.storage.TracksHistoryStorage
import org.guru.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import org.guru.playlistmaker.data.settings.impl.SettingsRepositoryImpl.Companion.APP_PREFERENCES
import org.guru.playlistmaker.data.storage.impl.TracksHistoryStorageImpl
import org.guru.playlistmaker.domain.search.TrackInteractor
import org.guru.playlistmaker.domain.search.TrackRepository
import org.guru.playlistmaker.domain.search.impl.TrackInteractorImpl
import org.guru.playlistmaker.domain.settings.SettingsInteractor
import org.guru.playlistmaker.domain.settings.SettingsRepository
import org.guru.playlistmaker.domain.settings.impl.SettingsInteractorImpl

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

    private fun getConfigurationAppRepository() : SettingsRepository {
        return SettingsRepositoryImpl(preferences)
    }

    fun getConfigurationAppInteractor() : SettingsInteractor {
        return SettingsInteractorImpl(getConfigurationAppRepository())
    }

}