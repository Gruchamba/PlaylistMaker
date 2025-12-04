package org.guru.playlistmaker.di.player

import android.media.MediaPlayer
import org.guru.playlistmaker.data.player.MediaPlayerClient
import org.guru.playlistmaker.data.player.PlayerClient
import org.guru.playlistmaker.data.player.impl.PlayerRepositoryImpl
import org.guru.playlistmaker.domain.player.PlayerInteractor
import org.guru.playlistmaker.domain.player.PlayerRepository
import org.guru.playlistmaker.domain.player.impl.PlayerInteractorImpl
import org.guru.playlistmaker.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerInteractorModule = module {
    factory<PlayerInteractor> { PlayerInteractorImpl(get()) }
}

val playerViewModelModule = module {
    viewModel { (url: String) -> PlayerViewModel(url) }
}

val playerRepositoryModule = module {
    factory<PlayerRepository> { PlayerRepositoryImpl(get()) }
}

val playerDataModule = module {
    factory<PlayerClient> { MediaPlayerClient() }

    factory { MediaPlayer() }
}