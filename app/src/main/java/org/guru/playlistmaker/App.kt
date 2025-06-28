package org.guru.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    val sharedPrefs: SharedPreferences by lazy { getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE) }
    var darkTheme = false

    override fun onCreate() {
        darkTheme = sharedPrefs.getBoolean(DARK_THEME_KEY, false)
        switchTheme(darkTheme)
        super.onCreate()
    }

    fun switchTheme(darkThemeEnabled: Boolean) {

        if (darkTheme != darkThemeEnabled)
            sharedPrefs.edit()
                .putBoolean(DARK_THEME_KEY, darkThemeEnabled)
                .apply()


        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    companion object {
        const val APP_PREFERENCES = "app_preferences"
        const val DARK_THEME_KEY = "dark_theme_key"
    }
}