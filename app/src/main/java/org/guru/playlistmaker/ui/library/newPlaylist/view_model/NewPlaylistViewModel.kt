package org.guru.playlistmaker.ui.library.newPlaylist.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.guru.playlistmaker.domain.library.playlist.PlaylistInteractor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class NewPlaylistViewModel : ViewModel(), KoinComponent {

    private val playlistInteractor: PlaylistInteractor by inject()

    fun createPlaylist(title: String, description: String?, imageUri: String?) {
        viewModelScope.launch {
            playlistInteractor.createPlaylist(
                title,
                description,
                imageUri
            )
        }
    }

}