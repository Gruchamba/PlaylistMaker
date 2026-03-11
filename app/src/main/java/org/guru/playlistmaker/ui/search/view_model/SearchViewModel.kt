package org.guru.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.guru.playlistmaker.domain.search.TrackInteractor
import org.guru.playlistmaker.domain.search.model.Track
import org.guru.playlistmaker.ui.search.fragment.SearchViewState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchViewModel : ViewModel(), KoinComponent {

    private val trackInteractor: TrackInteractor by inject()
    private var searchJob: Job? = null
    private var latestSearchText: String? = null

    private val searchViewState = MutableLiveData<SearchViewState>()
    fun observeState(): LiveData<SearchViewState> = searchViewState

    private val showToast = SingleLiveEvent<String?>()
    fun observeShowToast(): LiveData<String?> = showToast

    fun addTrackToHistory(track: Track) {
        trackInteractor.addTrackToHistory(track)
    }

    fun clearTracksHistory() {
        trackInteractor.clearTracksHistory()
    }

    fun readTracksFromHistory() {
        renderState(
            SearchViewState.ShowSearchHistory(
                trackInteractor.readTracksFromHistory()
            )
        )
    }

    fun searchRequest(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            renderState(SearchViewState.Loading())

            viewModelScope.launch {
                trackInteractor.searchTracks(newSearchText).collect { pair ->
                    processResult(pair.first, pair.second)
                }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        val trackList = mutableListOf<Track>()
        if (foundTracks != null)
            trackList.addAll(foundTracks)

        when {
            errorMessage != null -> {
                renderState(SearchViewState.Error())
                showToast.postValue(errorMessage)
            }

            trackList.isEmpty() -> renderState(SearchViewState.Empty())
            else -> renderState(SearchViewState.Content(foundTracks!!))
        }
    }

    private fun renderState(state: SearchViewState) {
        searchViewState.postValue(state)
    }

    fun setDefaultState() {
        renderState(SearchViewState.DefaultState())
    }

    fun searchDebounce(changedText: String) {

        if (changedText.isEmpty() || latestSearchText == changedText)
            return

        latestSearchText = changedText

        cancelSearch()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchRequest(changedText)
        }

    }

    fun cancelSearch() {
        searchJob?.cancel()
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}