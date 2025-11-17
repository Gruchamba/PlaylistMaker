package org.guru.playlistmaker.util

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import org.guru.playlistmaker.Creator
import org.guru.playlistmaker.data.configuration.ConfigurationAppRepositoryImpl.Companion.APP_PREFERENCES

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        switchTheme(Creator.getConfigurationAppInteractor(getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)).isDarkTheme())
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}