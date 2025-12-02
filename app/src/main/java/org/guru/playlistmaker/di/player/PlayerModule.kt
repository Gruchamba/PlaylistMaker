package org.guru.playlistmaker.di.player

import org.guru.playlistmaker.data.player.MediaPlayerClient
import org.guru.playlistmaker.data.player.PlayerClient
import org.guru.playlistmaker.data.player.impl.PlayerRepositoryImpl
import org.guru.playlistmaker.domain.player.PlayerInteractor
import org.guru.playlistmaker.domain.player.PlayerRepository
import org.guru.playlistmaker.domain.player.impl.PlayerInteractorImpl
import org.guru.playlistmaker.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerModule = module {

    factory<PlayerInteractor> { PlayerInteractorImpl(get()) }

    viewModel { (url: String) -> PlayerViewModel(url) }

}

val playerDataModule = module {

    factory<PlayerRepository> { PlayerRepositoryImpl(get()) }

    factory<PlayerClient> { MediaPlayerClient() }
}