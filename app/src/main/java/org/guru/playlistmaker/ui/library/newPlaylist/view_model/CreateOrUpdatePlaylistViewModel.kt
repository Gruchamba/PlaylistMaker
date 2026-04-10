package org.guru.playlistmaker.ui.library.newPlaylist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.guru.playlistmaker.domain.library.playlist.PlaylistInteractor
import org.guru.playlistmaker.domain.library.playlist.model.Playlist
import org.guru.playlistmaker.ui.library.newPlaylist.fragment.CreateOrUpdateStateView
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class CreateOrUpdatePlaylistViewModel : ViewModel(), KoinComponent {

    private val playlistInteractor: PlaylistInteractor by inject()

    private val playlistStateLiveData = MutableLiveData<CreateOrUpdateStateView>()
    fun observePlaylistState(): LiveData<CreateOrUpdateStateView> = playlistStateLiveData

    private var playlist: Playlist? = null

    fun setPlaylist(playlist: Playlist?) {
        this.playlist = playlist
        playlistStateLiveData.postValue(
            if (playlist == null) CreateOrUpdateStateView.CreateState
            else CreateOrUpdateStateView.UpdateState(playlist)
        )
    }

    fun createPlaylist(title: String, description: String?, imageUri: String?) {
        viewModelScope.launch {
            playlistInteractor.createPlaylist(
                title,
                description,
                imageUri
            )
        }
    }

    fun updatePlaylist(title: String, description: String?, imageUri: String?) {
        playlist?.let {
            it.title = title
            it.description = description
            it.uriImage = imageUri
        }

        viewModelScope.launch {
            playlistInteractor.updatePlaylist(playlist!!)
        }
    }

}