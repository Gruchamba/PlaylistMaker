package org.guru.playlistmaker.domain.api

interface ConfigurationAppRepository {

    fun isDarkTheme() : Boolean

    fun initializeTheme()

    fun switchTheme(darkThemeEnabled: Boolean)
}