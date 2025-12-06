package org.guru.playlistmaker.domain.settings

interface SettingsInteractor {

    fun isDarkTheme() : Boolean

    fun switchTheme(darkThemeEnabled: Boolean)
}