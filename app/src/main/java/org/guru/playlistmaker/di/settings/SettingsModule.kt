package org.guru.playlistmaker.di.settings

import android.content.Context
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import org.guru.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import org.guru.playlistmaker.data.settings.impl.SettingsRepositoryImpl.Companion.APP_PREFERENCES
import org.guru.playlistmaker.domain.settings.SettingsInteractor
import org.guru.playlistmaker.domain.settings.SettingsRepository
import org.guru.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import org.guru.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

var settingsInteractorModule = module {
    single<SettingsInteractor> { SettingsInteractorImpl(get()) }
}

var settingsViewModelModule = module {
    viewModel { SettingsViewModel() }
}

var settingsDataModule = module {
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }

    single { get<Context>().getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE) }
}