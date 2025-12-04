package org.guru.playlistmaker.app

import android.app.Application
import org.guru.playlistmaker.di.player.playerDataModule
import org.guru.playlistmaker.di.player.playerInteractorModule
import org.guru.playlistmaker.di.player.playerRepositoryModule
import org.guru.playlistmaker.di.player.playerViewModelModule
import org.guru.playlistmaker.di.search.searchDataModule
import org.guru.playlistmaker.di.search.searchInteractorModule
import org.guru.playlistmaker.di.search.searchRepositoryModule
import org.guru.playlistmaker.di.search.searchViewModelModule
import org.guru.playlistmaker.di.settings.settingsDataModule
import org.guru.playlistmaker.di.settings.settingsInteractorModule
import org.guru.playlistmaker.di.settings.settingsViewModelModule
import org.guru.playlistmaker.domain.settings.SettingsRepository
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                playerInteractorModule, playerViewModelModule, playerRepositoryModule, playerDataModule,
                settingsInteractorModule, settingsViewModelModule, settingsDataModule,
                searchInteractorModule, searchViewModelModule, searchRepositoryModule, searchDataModule
            )
        }
        val settingsRepository: SettingsRepository = getKoin().get()
        settingsRepository.initializeTheme()
    }

}