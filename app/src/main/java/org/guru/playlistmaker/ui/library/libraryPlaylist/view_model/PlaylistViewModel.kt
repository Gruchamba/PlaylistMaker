package org.guru.playlistmaker.ui.library.libraryPlaylist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.guru.playlistmaker.domain.library.playlist.PlaylistInteractor
import org.guru.playlistmaker.ui.library.libraryPlaylist.fragment.PlaylistViewState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlaylistViewModel : ViewModel(), KoinComponent {

    private val playlistInteractor: PlaylistInteractor by inject()

    private val playlistViewStateLiveData = MutableLiveData<PlaylistViewState>()
    fun observePlaylistViewState(): LiveData<PlaylistViewState> = playlistViewStateLiveData

    fun loadAllPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect {
                if (it.isEmpty()) renderState(PlaylistViewState.Empty)
                else renderState(PlaylistViewState.Content(it.reversed()))
            }
        }
    }

    private fun renderState(state: PlaylistViewState) {
        playlistViewStateLiveData.postValue(state)
    }


}