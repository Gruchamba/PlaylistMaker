package org.guru.playlistmaker.domain.api

interface ConfigurationInteractor {

    fun isDarkTheme() : Boolean

    fun switchTheme(darkThemeEnabled: Boolean)
}