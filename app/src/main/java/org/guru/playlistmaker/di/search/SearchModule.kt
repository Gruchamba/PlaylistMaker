package org.guru.playlistmaker.di.search

import android.content.Context
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.guru.playlistmaker.data.NetworkClient
import org.guru.playlistmaker.data.search.ItunesApiService
import org.guru.playlistmaker.data.search.RetrofitNetworkClient
import org.guru.playlistmaker.data.search.impl.SearchTrackRepositoryImpl
import org.guru.playlistmaker.data.search.storage.TracksHistoryStorage
import org.guru.playlistmaker.data.settings.impl.SettingsRepositoryImpl.Companion.APP_PREFERENCES
import org.guru.playlistmaker.data.storage.impl.TracksHistoryStorageImpl
import org.guru.playlistmaker.domain.search.SearchTrackInteractor
import org.guru.playlistmaker.domain.search.SearchTrackRepository
import org.guru.playlistmaker.domain.search.impl.SearchTrackInteractorImpl
import org.guru.playlistmaker.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

private const val itunesURL = "https://itunes.apple.com/"

val searchInteractorModule = module {
    single<SearchTrackInteractor> { SearchTrackInteractorImpl(get()) }
}

val searchViewModelModule = module {
    viewModel { SearchViewModel() }
}

val searchRepositoryModule = module {
    single<SearchTrackRepository> {
        SearchTrackRepositoryImpl(get(), get(), get()) }
}

val searchDataModule = module {

    single<NetworkClient> { RetrofitNetworkClient() }

    single<HttpLoggingInterceptor> {
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }

    single<ItunesApiService> {
        Retrofit.Builder()
//            .client(get<OkHttpClient>())
            .baseUrl(itunesURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create<ItunesApiService>()
    }

    factory { Gson() }

    single<TracksHistoryStorage> { TracksHistoryStorageImpl(get()) }

    single { get<Context>().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE) }

}