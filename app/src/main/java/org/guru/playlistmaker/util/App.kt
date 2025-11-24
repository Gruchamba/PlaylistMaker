package org.guru.playlistmaker.util

import android.app.Application
import org.guru.playlistmaker.creator.Creator

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Creator.initialize(this@App)
    }

}