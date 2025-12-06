package org.guru.playlistmaker.domain.settings.impl

import org.guru.playlistmaker.domain.settings.SettingsInteractor
import org.guru.playlistmaker.domain.settings.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository) :
    SettingsInteractor {

    override fun isDarkTheme(): Boolean {
        return settingsRepository.isDarkTheme()
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        settingsRepository.switchTheme(darkThemeEnabled)
    }

}