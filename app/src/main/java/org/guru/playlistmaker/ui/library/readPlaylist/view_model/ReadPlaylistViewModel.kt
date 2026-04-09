package org.guru.playlistmaker.ui.library.readPlaylist.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.guru.playlistmaker.domain.library.playlist.PlaylistInteractor
import org.guru.playlistmaker.domain.library.playlist.model.Playlist
import org.guru.playlistmaker.domain.search.model.Track
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ReadPlaylistViewModel: ViewModel(), KoinComponent {

    private val playlistInteractor: PlaylistInteractor by inject()

    private val playlistStateLiveData = MutableLiveData<Playlist>()
    fun observePlayerState(): LiveData<Playlist> = playlistStateLiveData

    private val tracksStateLiveData = MutableLiveData<List<Track>>()
    fun observeTracksState(): LiveData<List<Track>> = tracksStateLiveData

    fun setPlaylistId(playlistId: Int) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistById(playlistId).collect { playlist ->
                playlistStateLiveData.postValue(playlist)

                playlistInteractor.getTracksForPlaylist(playlist.tracksIdList).collect {
                    tracksStateLiveData.postValue(it)
                }
            }
        }



    }

}