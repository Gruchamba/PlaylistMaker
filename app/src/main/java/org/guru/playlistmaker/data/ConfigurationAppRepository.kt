package org.guru.playlistmaker.data

interface ConfigurationAppRepository {

    fun isDarkTheme() : Boolean

    fun saveTheme(darkThemeEnabled: Boolean)
}