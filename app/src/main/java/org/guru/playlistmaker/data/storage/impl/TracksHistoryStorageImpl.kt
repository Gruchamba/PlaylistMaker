package org.guru.playlistmaker.data.storage.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import org.guru.playlistmaker.data.search.storage.TracksHistoryStorage
import org.guru.playlistmaker.domain.search.model.Track
import androidx.core.content.edit

class TracksHistoryStorageImpl(private val sharedPreferences: SharedPreferences) :
    TracksHistoryStorage {

    override fun addTrackToHistory(track: Track) {

        val tracksFromSearchHistory = readTracksFromHistory().toMutableList()
        tracksFromSearchHistory.removeIf { it.trackId == track.trackId }

        if (tracksFromSearchHistory.size >= MAX_HISTORY_SIZE) {
            tracksFromSearchHistory.removeAt(tracksFromSearchHistory.size - 1)
        }
        tracksFromSearchHistory.add(0, track)

        sharedPreferences.edit {
            putString(SEARCH_HISTORY_KEY, Gson().toJson(tracksFromSearchHistory))
        }
    }

    override fun clearTracksHistory() {
        sharedPreferences.edit { remove(SEARCH_HISTORY_KEY) }
    }

    override fun readTracksFromHistory(): List<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null) ?: return ArrayList(
            MAX_HISTORY_SIZE )
        val tracksList = ArrayList(Gson().fromJson(json, Array<Track>::class.java).toList())
        return tracksList
    }

    companion object {
        private const val SEARCH_HISTORY_KEY = "search_history"
        private const val MAX_HISTORY_SIZE = 10
    }
}