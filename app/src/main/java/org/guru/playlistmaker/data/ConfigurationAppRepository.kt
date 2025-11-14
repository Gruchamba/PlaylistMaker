package org.guru.playlistmaker.data

interface ConfigurationAppRepository {

    fun isDarkTheme() : Boolean

    fun switchTheme(darkThemeEnabled: Boolean)
}