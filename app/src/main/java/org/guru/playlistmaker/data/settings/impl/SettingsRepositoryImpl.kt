package org.guru.playlistmaker.data.settings.impl

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import org.guru.playlistmaker.domain.settings.SettingsRepository
import androidx.core.content.edit

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SettingsRepository {

    private var darkTheme = sharedPreferences.getBoolean(DARK_THEME_KEY, false)

    override fun isDarkTheme(): Boolean {
        return darkTheme
    }

    override fun initializeTheme() {
        switchTheme(isDarkTheme())
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        if (darkTheme != darkThemeEnabled)
            sharedPreferences.edit {
                putBoolean(DARK_THEME_KEY, darkThemeEnabled)
            }

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        darkTheme = darkThemeEnabled
    }

    companion object {
        const val APP_PREFERENCES = "app_preferences"
        const val DARK_THEME_KEY = "dark_theme_key"
    }
}