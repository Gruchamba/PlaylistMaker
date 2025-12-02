package org.guru.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.guru.playlistmaker.domain.search.TrackInteractor
import org.guru.playlistmaker.domain.search.model.Track
import org.guru.playlistmaker.ui.search.activity.SearchViewState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchViewModel : ViewModel(), KoinComponent {

    private val trackInteractor: TrackInteractor by inject()
    private val handler = Handler(Looper.getMainLooper())
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

            trackInteractor.searchTracks(newSearchText, object: TrackInteractor.TrackConsumer {
                override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                    handler.post {
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
                }
            })

        }

    }

    private fun renderState(state: SearchViewState) {
        searchViewState.postValue(state)
    }

    fun setDefaultState() {
        renderState(SearchViewState.DefaultState())
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText)
            return

        latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        val searchRunnable = Runnable { searchRequest(changedText) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun onDestroy() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}