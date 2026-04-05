package org.guru.playlistmaker.di.library

import androidx.room.Room
import org.guru.playlistmaker.data.db.AppDatabase
import org.guru.playlistmaker.data.db.converters.PlaylistDbConverter
import org.guru.playlistmaker.data.db.converters.TrackDbConverter
import org.guru.playlistmaker.data.db.converters.TrackForPlaylistDbConverter
import org.guru.playlistmaker.data.db.dao.PlaylistDao
import org.guru.playlistmaker.data.db.dao.FavoriteTrackDao
import org.guru.playlistmaker.data.db.dao.TrackForPlaylistDao
import org.guru.playlistmaker.data.favorites.TrackRepositoryImpl
import org.guru.playlistmaker.data.playlist.PlaylistRepositoryImpl
import org.guru.playlistmaker.domain.library.favorites.FavoritesTrackInteractor
import org.guru.playlistmaker.domain.library.favorites.TrackRepository
import org.guru.playlistmaker.domain.library.favorites.impl.FavoritesTrackInteractorImpl
import org.guru.playlistmaker.domain.library.playlist.PlaylistInteractor
import org.guru.playlistmaker.domain.library.playlist.PlaylistRepository
import org.guru.playlistmaker.domain.library.playlist.impl.PlaylistInteractorImpl
import org.guru.playlistmaker.ui.library.favorites.view_model.FavoritesTrackViewModel
import org.guru.playlistmaker.ui.library.newPlaylist.view_model.NewPlaylistViewModel
import org.guru.playlistmaker.ui.library.playlist.view_model.PlaylistViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoritesTrackViewModelModule = module {
    viewModel { FavoritesTrackViewModel() }
}

val libraryViewModelModule = module {
    viewModel { PlaylistViewModel() }
    viewModel { NewPlaylistViewModel() }
}

val libraryRepositoryModule = module {
    factory { TrackForPlaylistDbConverter() }
    factory { TrackDbConverter() }

    single<TrackRepository> {
        TrackRepositoryImpl(get(), get())
    }

    single<FavoritesTrackInteractor> {
        FavoritesTrackInteractorImpl(get())
    }

    factory { PlaylistDbConverter() }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(
            get(),
            get(),
            get(),
            get()
        )
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get())
    }
}

val libraryDataModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .build()
    }

    single<FavoriteTrackDao> {
        get<AppDatabase>().favoriteTrackDao()
    }

    single<PlaylistDao> {
        get<AppDatabase>().playlistDao()
    }

    single<TrackForPlaylistDao> {
        get<AppDatabase>().trackForPlaylistDao()
    }


}