package org.guru.playlistmaker.di.library

import androidx.room.Room
import org.guru.playlistmaker.data.db.AppDatabase
import org.guru.playlistmaker.data.db.converters.TrackDbConverter
import org.guru.playlistmaker.ui.library.favorites.view_model.FavoritesTrackViewModel
import org.guru.playlistmaker.ui.library.playlist.view_model.PlaylistViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoritesTrackViewModelModule = module {
    viewModel { FavoritesTrackViewModel() }
}

val playlistViewModelModule = module {
    viewModel { PlaylistViewModel() }
}

val libraryRepositoryModule = module {
    factory { TrackDbConverter() }
}

val libraryDataModule = module {
    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
}