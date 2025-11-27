package org.guru.playlistmaker.presentation.search

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.guru.playlistmaker.creator.Creator
import org.guru.playlistmaker.domain.api.TrackInteractor
import org.guru.playlistmaker.domain.models.Track
import org.guru.playlistmaker.util.SingleLiveEvent

class SearchViewModel : ViewModel() {

    private val TAG = SearchViewModel::class.java.name
    private val trackInteractor: TrackInteractor by lazy { Creator.provideTracksInteractor() }
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
        Log.i(TAG, "read track from history")
        renderState(
            SearchViewState.ShowSearchHistory(
                trackInteractor.readTracksFromHistory()
            )
        )
    }

    fun searchRequest(newSearchText: String) {
        Log.i(TAG, "send search request: $newSearchText")
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
        Log.i(TAG, "render post state: $state")
        searchViewState.postValue(state)
    }

    fun setDefaultState() {
        renderState(SearchViewState.DefaultState())
    }

    fun searchDebounce(changedText: String) {
        Log.i(TAG, "search debounce: $changedText")
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
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

        fun getFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel()
            }
        }
    }
}