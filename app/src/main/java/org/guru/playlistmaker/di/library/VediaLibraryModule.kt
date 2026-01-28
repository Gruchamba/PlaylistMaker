package org.guru.playlistmaker.di.library

import org.guru.playlistmaker.ui.library.favorites.view_model.FavoritesTrackViewModel
import org.guru.playlistmaker.ui.library.playlist.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val favoritesTrackViewModelModule = module {
    viewModel { FavoritesTrackViewModel() }
}

val playlistViewModelModule = module {
    viewModel { PlaylistViewModel() }
}