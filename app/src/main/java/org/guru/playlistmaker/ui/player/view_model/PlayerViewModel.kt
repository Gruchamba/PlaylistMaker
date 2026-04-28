package org.guru.playlistmaker.ui.player.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.guru.playlistmaker.data.db.dao.FavoriteTrackDao
import org.guru.playlistmaker.domain.library.favorites.FavoritesTrackInteractor
import org.guru.playlistmaker.domain.library.playlist.PlaylistInteractor
import org.guru.playlistmaker.domain.library.playlist.model.Playlist
import org.guru.playlistmaker.domain.player.model.MusicServiceControl
import org.guru.playlistmaker.domain.player.model.PlayerState
import org.guru.playlistmaker.domain.search.model.Track
import org.guru.playlistmaker.ui.player.fragment.AddInPlaylistState
import org.guru.playlistmaker.ui.player.fragment.PlayerViewState
import org.guru.playlistmaker.ui.player.fragment.PlayerViewState.LoadPlaylists
import org.guru.playlistmaker.ui.search.view_model.SingleLiveEvent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PlayerViewModel(
    private val track: Track,
    private val favoriteTrackDao: FavoriteTrackDao
) : ViewModel(), KoinComponent {

    private companion object {
        val TAG: String = PlayerViewModel::class.java.name
    }

    private val favoritesTrackInteractor: FavoritesTrackInteractor by inject()
    private val playlistInteractor: PlaylistInteractor by inject()

    private val playerStateLiveData = MutableLiveData<PlayerViewState>()
    fun observePlayerState(): LiveData<PlayerViewState> = playerStateLiveData

    private val favoriteStateLiveData = MutableLiveData<Boolean>()
    fun observeFavoriteState(): LiveData<Boolean> = favoriteStateLiveData

    private val addInPlaylistState = SingleLiveEvent<AddInPlaylistState>()
    fun observeAddInPlaylistState(): LiveData<AddInPlaylistState> = addInPlaylistState

    private var musicServiceControl: MusicServiceControl? = null
    fun setAudioPlayerControl(musicServiceControl: MusicServiceControl) {
        this.musicServiceControl = musicServiceControl

        viewModelScope.launch {
            musicServiceControl.getPlayerViewState().collect {
                playerStateLiveData.postValue(it)
            }
        }
    }

    init {
        viewModelScope.launch {
            track.isFavorite = favoriteTrackDao.getAllFavoriteTracksIds().contains(track.trackId)
            favoriteStateLiveData.postValue(track.isFavorite)
        }
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceControl = null
    }

    fun onPlayButtonClicked() {
        when(musicServiceControl?.getPlayerState()) {
            PlayerState.STATE_PLAYING -> musicServiceControl?.pausePlayer()
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> musicServiceControl?.startPlayer()
            else -> { Log.e(TAG, "error player state ${playerStateLiveData.value}") }
        }
    }

    fun addNotification() {
        musicServiceControl?.addNotification()
    }

    fun removeNotification() {
        musicServiceControl?.removeNotification()
    }

    fun removeAudioPlayerControl() {
        musicServiceControl = null
    }

    private fun renderState(state: PlayerViewState) {
        playerStateLiveData.postValue(state)
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            if (track.isFavorite) favoritesTrackInteractor.deleteFromFavorites(track)
            else favoritesTrackInteractor.addTrackToFavorites(track)
        }
        track.isFavorite = !track.isFavorite
        favoriteStateLiveData.postValue(track.isFavorite)
    }

    fun loadPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect {
                renderState(LoadPlaylists(it))
            }
        }
    }

    fun addTrackInPlaylist(playlist: Playlist, track: Track) {

        if (playlist.tracksIdList.contains(track.trackId)) {
            addInPlaylistState.postValue(AddInPlaylistState.AlreadyExist(playlist.title))

        } else {
            viewModelScope.launch {
                playlistInteractor.addTrackForPlaylist(playlist, track).collect {
                    addInPlaylistState.postValue(AddInPlaylistState.Successful(playlist.title))
                }
            }
        }
    }

}