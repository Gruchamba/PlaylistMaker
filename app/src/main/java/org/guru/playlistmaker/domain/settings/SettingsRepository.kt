package org.guru.playlistmaker.domain.settings

interface SettingsRepository {

    fun isDarkTheme() : Boolean

    fun initializeTheme()

    fun switchTheme(darkThemeEnabled: Boolean)
}