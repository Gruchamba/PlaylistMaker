package org.guru.playlistmaker.data.storage

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import org.guru.playlistmaker.data.TracksHistoryStorage
import org.guru.playlistmaker.domain.models.Track

class TracksHistoryStorageImpl(private val sharedPreferences: SharedPreferences) : TracksHistoryStorage {

    private val TAG = TracksHistoryStorageImpl::class.java.name

    override fun addTrackToHistory(track: Track) {
        Log.d(TAG, "add track: $track")

        val tracksFromSearchHistory = readTracksFromHistory().toMutableList()
        tracksFromSearchHistory.removeIf { it.trackId == track.trackId }

        if (tracksFromSearchHistory.size >= MAX_HISTORY_SIZE) {
            tracksFromSearchHistory.removeAt(tracksFromSearchHistory.size - 1)
        }
        tracksFromSearchHistory.add(0, track)

        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, Gson().toJson(tracksFromSearchHistory))
            .apply()
    }

    override fun clearTracksHistory() {
        Log.d(TAG, "clear history")
        sharedPreferences.edit()
            .remove(SEARCH_HISTORY_KEY)
            .apply()
    }

    override fun readTracksFromHistory(): List<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null) ?: return ArrayList(MAX_HISTORY_SIZE )
        val tracksList = ArrayList(Gson().fromJson(json, Array<Track>::class.java).toList())
        Log.d(TAG, "read from sharedPreferences: ${tracksList.size}")
        return tracksList
    }

    companion object {
        private const val SEARCH_HISTORY_KEY = "search_history"
        private const val MAX_HISTORY_SIZE = 10
    }
}