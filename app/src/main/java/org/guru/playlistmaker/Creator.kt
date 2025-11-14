package org.guru.playlistmaker

import android.app.Application.MODE_PRIVATE
import android.content.Context
import org.guru.playlistmaker.data.TrackRepositoryImpl
import org.guru.playlistmaker.data.TracksHistoryStorage
import org.guru.playlistmaker.data.network.RetrofitNetworkClient
import org.guru.playlistmaker.data.storage.TracksHistoryStorageImpl
import org.guru.playlistmaker.domain.api.TrackInteractor
import org.guru.playlistmaker.domain.api.TrackRepository
import org.guru.playlistmaker.domain.impl.TrackInteractorImpl
import org.guru.playlistmaker.ui.App.Companion.APP_PREFERENCES

object Creator {

    private fun getTracksRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(getTracksHistoryStorage(context), RetrofitNetworkClient())
    }

    fun provideTracksInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getTracksRepository(context))
    }

    fun getTracksHistoryStorage(context: Context) : TracksHistoryStorage {
        return TracksHistoryStorageImpl(context.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE))
    }

}