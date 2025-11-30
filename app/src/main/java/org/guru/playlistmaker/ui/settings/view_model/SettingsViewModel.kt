package org.guru.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.guru.playlistmaker.creator.Creator
import org.guru.playlistmaker.domain.settings.SettingsInteractor

class SettingsViewModel : ViewModel() {

    private val settingsInteractor: SettingsInteractor by lazy {
        Creator.getConfigurationAppInteractor()
    }

    companion object {

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel()
            }
        }
    }

    private val isDarkThemeLiveData = MutableLiveData(settingsInteractor.isDarkTheme())
    fun observeDarkTheme(): LiveData<Boolean> = isDarkThemeLiveData

    fun switchTheme(checked: Boolean) {
        settingsInteractor.switchTheme(checked)
    }

}