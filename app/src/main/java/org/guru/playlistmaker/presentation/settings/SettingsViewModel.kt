package org.guru.playlistmaker.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.guru.playlistmaker.creator.Creator
import org.guru.playlistmaker.domain.api.ConfigurationInteractor

class SettingsViewModel : ViewModel() {

    private val TAG = SettingsViewModel::class.java.name

    private val configurationInteractor: ConfigurationInteractor by lazy {
        Creator.getConfigurationAppInteractor()
    }

    companion object {

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel()
            }
        }
    }

    private val isDarkThemeLiveData = MutableLiveData(configurationInteractor.isDarkTheme())
    fun observeDarkTheme(): LiveData<Boolean> = isDarkThemeLiveData

    fun switchTheme(checked: Boolean) {
        configurationInteractor.switchTheme(checked)
    }

}