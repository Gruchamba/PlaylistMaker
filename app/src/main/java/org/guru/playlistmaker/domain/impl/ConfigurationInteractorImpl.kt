package org.guru.playlistmaker.domain.impl

import org.guru.playlistmaker.domain.api.ConfigurationAppRepository
import org.guru.playlistmaker.domain.api.ConfigurationInteractor

class ConfigurationInteractorImpl(private val configurationAppRepository: ConfigurationAppRepository) : ConfigurationInteractor {

    override fun isDarkTheme(): Boolean {
        return configurationAppRepository.isDarkTheme()
    }

    override fun switchTheme(darkThemeEnabled: Boolean) {
        configurationAppRepository.switchTheme(darkThemeEnabled)
    }

}