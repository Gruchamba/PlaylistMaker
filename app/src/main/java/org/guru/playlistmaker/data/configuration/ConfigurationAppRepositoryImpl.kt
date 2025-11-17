package org.guru.playlistmaker.data.configuration

import android.content.SharedPreferences
import org.guru.playlistmaker.data.ConfigurationAppRepository

class ConfigurationAppRepositoryImpl(private val sharedPreferences: SharedPreferences) : ConfigurationAppRepository {

    private var darkTheme = sharedPreferences.getBoolean(DARK_THEME_KEY, false)

    override fun isDarkTheme(): Boolean {
        return darkTheme
    }

    override fun saveTheme(darkThemeEnabled: Boolean) {
        if (darkTheme != darkThemeEnabled)
            sharedPreferences.edit()
                .putBoolean(DARK_THEME_KEY, darkThemeEnabled)
                .apply()

        darkTheme = darkThemeEnabled
    }

    companion object {
        const val APP_PREFERENCES = "app_preferences"
        const val DARK_THEME_KEY = "dark_theme_key"
    }
}