package org.guru.playlistmaker.app

import android.app.Application
import org.guru.playlistmaker.creator.Creator
import org.guru.playlistmaker.di.player.playerDataModule
import org.guru.playlistmaker.di.player.playerModule
import org.guru.playlistmaker.di.settings.settingsDataModule
import org.guru.playlistmaker.di.settings.settingsModule
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
                playerModule, playerDataModule,
                settingsModule, settingsDataModule
            )
        }
        val settingsRepository: SettingsRepository = getKoin().get()
        settingsRepository.initializeTheme()
        Creator.initialize(this@App)
    }

}