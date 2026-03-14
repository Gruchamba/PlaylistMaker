package org.guru.playlistmaker.ui.library.favorites.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.guru.playlistmaker.domain.library.favorites.FavoritesTrackInteractor
import org.guru.playlistmaker.ui.library.favorites.fragment.FavoritesViewState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class FavoritesTrackViewModel : ViewModel(), KoinComponent {

    private val favoritesTrackInteractor: FavoritesTrackInteractor by inject()

    private val favoritesViewStateLiveData = MutableLiveData<FavoritesViewState>()
    fun observeFavoritesState(): LiveData<FavoritesViewState> = favoritesViewStateLiveData

    fun loadFavoritesTrack() {
        viewModelScope.launch {
            favoritesTrackInteractor.getAllFavoriteTracks().collect {
                if (it.isEmpty()) renderState(FavoritesViewState.Empty())
                else renderState(FavoritesViewState.Content(it))
            }
        }
    }

    private fun renderState(state: FavoritesViewState) {
        Log.d("FAVORITE", "render state: $state")
        favoritesViewStateLiveData.postValue(state)
    }
}