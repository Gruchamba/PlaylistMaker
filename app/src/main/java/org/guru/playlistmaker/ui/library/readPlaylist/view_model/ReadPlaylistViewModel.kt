package org.guru.playlistmaker.ui.library.readPlaylist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.guru.playlistmaker.domain.library.playlist.PlaylistInteractor
import org.guru.playlistmaker.domain.library.playlist.model.Playlist
import org.guru.playlistmaker.domain.search.model.Track
import org.guru.playlistmaker.ui.library.readPlaylist.fragment.ReadPlaylistFragmentViewState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ReadPlaylistViewModel: ViewModel(), KoinComponent {

    private val playlistInteractor: PlaylistInteractor by inject()

    private val removePlaylistStateLiveData = MutableLiveData<Boolean>()
    fun observeRemovePlayerState(): LiveData<Boolean> = removePlaylistStateLiveData

    private val playlistStateLiveData = MutableLiveData<Playlist>()
    fun observePlayerState(): LiveData<Playlist> = playlistStateLiveData

    private val tracksStateLiveData = MutableLiveData<ReadPlaylistFragmentViewState>()
    fun observeTracksState(): LiveData<ReadPlaylistFragmentViewState> = tracksStateLiveData

    fun setPlaylistId(playlistId: Int) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(playlistId).collect { playlist ->
                playlistStateLiveData.postValue(playlist)

                playlistInteractor.getTracksForPlaylist(playlist.tracksIdList).collect {
                    renderReadPlaylistFragmentViewState(it)
                }
            }
        }
    }

    fun removeTrackFromPlaylist(trackId: String, tracks: List<Track>) {
        if (playlistStateLiveData.value != null) {
            viewModelScope.launch {
                playlistInteractor.removeTrackFromPlaylist(
                    playlistStateLiveData.value!!,
                    tracks,
                    trackId
                ).collect {
                    renderReadPlaylistFragmentViewState(it)
                }
            }
        }
    }

    fun removePlaylist() {
        viewModelScope.launch {
            playlistInteractor.removePlaylist(playlistStateLiveData.value!!).collect {
                removePlaylistStateLiveData.postValue(it)
            }
        }
    }

    private fun renderReadPlaylistFragmentViewState(list: List<Track>) {
        tracksStateLiveData.postValue(
            if (list.isEmpty()) ReadPlaylistFragmentViewState.Empty
            else ReadPlaylistFragmentViewState.Content(list)
        )
    }

    fun getPlaylist() : Playlist? {
        return playlistStateLiveData.value
    }

}