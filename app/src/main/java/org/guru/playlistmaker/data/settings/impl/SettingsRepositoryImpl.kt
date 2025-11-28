package org.guru.playlistmaker.data.settings.impl

import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import org.guru.playlistmaker.domain.settings.SettingsRepository

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) :
    SettingsRepository {

    val TAG = SettingsRepositoryImpl::class.java.name

    private var darkTheme = sharedPreferences.getBoolean(DARK_THEME_KEY, false)

    override fun isDarkTheme(): Boolean {
        return darkTheme
    }

    override fun initializeTheme() {
        switchTheme(isDarkTheme())
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        if (darkTheme != darkThemeEnabled)
            sharedPreferences.edit()
                .putBoolean(DARK_THEME_KEY, darkThemeEnabled)
                .apply()

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )

        darkTheme = darkThemeEnabled
        Log.i(TAG, "Change theme ${if (darkThemeEnabled) "dark" else "light"}")
    }

    companion object {
        const val APP_PREFERENCES = "app_preferences"
        const val DARK_THEME_KEY = "dark_theme_key"
    }
}