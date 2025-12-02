package org.guru.playlistmaker.app

import android.app.Application
import org.guru.playlistmaker.creator.Creator
import org.guru.playlistmaker.di.player.playerDataModule
import org.guru.playlistmaker.di.player.playerModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(playerModule, playerDataModule)
        }
        Creator.initialize(this@App)
    }

}