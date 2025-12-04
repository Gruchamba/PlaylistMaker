package org.guru.playlistmaker.di.search

import android.content.Context
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.google.gson.Gson
import org.guru.playlistmaker.data.NetworkClient
import org.guru.playlistmaker.data.search.ItunesApiService
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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

private const val itunesURL = "https://itunes.apple.com/"

val searchInteractorModule = module {
    single<TrackInteractor> { TrackInteractorImpl(get()) }
}

val searchViewModelModule = module {
    viewModel { SearchViewModel() }
}

val searchRepositoryModule = module {
    single<TrackRepository> { TrackRepositoryImpl(get(), get()) }
}

val searchDataModule = module {

    single<NetworkClient> { RetrofitNetworkClient() }

    single<ItunesApiService> {
        Retrofit.Builder()
            .baseUrl(itunesURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create<ItunesApiService>()
    }

    factory { Gson() }

    single<TracksHistoryStorage> { TracksHistoryStorageImpl(get()) }

    single { get<Context>().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE) }

}