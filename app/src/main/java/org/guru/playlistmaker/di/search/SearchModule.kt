package org.guru.playlistmaker.di.search

import android.content.Context
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import org.guru.playlistmaker.data.NetworkClient
import org.guru.playlistmaker.data.search.RetrofitNetworkClient
import org.guru.playlistmaker.data.search.impl.TrackRepositoryImpl
import org.guru.playlistmaker.data.search.storage.TracksHistoryStorage
import org.guru.playlistmaker.data.settings.impl.SettingsRepositoryImpl.Companion.APP_PREFERENCES
import org.guru.playlistmaker.data.storage.impl.TracksHistoryStorageImpl
import org.guru.playlistmaker.domain.search.TrackInteractor
import org.guru.playlistmaker.domain.search.TrackRepository
import org.guru.playlistmaker.domain.search.impl.TrackInteractorImpl
import org.guru.playlistmaker.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchModule = module {

    single<TrackInteractor> { TrackInteractorImpl(get()) }

    viewModel { SearchViewModel() }
}

val searchDataModule = module {

    single<TrackRepository> { TrackRepositoryImpl(get(), get()) }

    single<NetworkClient> { RetrofitNetworkClient() }

    single<TracksHistoryStorage> {
        TracksHistoryStorageImpl(
            get<Context>().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        )
    }
}