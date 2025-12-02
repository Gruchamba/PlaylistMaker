package org.guru.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.guru.playlistmaker.domain.settings.SettingsInteractor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SettingsViewModel : ViewModel(), KoinComponent {

    private val settingsInteractor: SettingsInteractor by inject()

    private val isDarkThemeLiveData = MutableLiveData(settingsInteractor.isDarkTheme())
    fun observeDarkTheme(): LiveData<Boolean> = isDarkThemeLiveData

    fun switchTheme(checked: Boolean) {
        settingsInteractor.switchTheme(checked)
    }

}