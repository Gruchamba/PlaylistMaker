package org.guru.playlistmaker.domain.models

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    private val TAG = SearchHistory::class.java.name

    fun addTrack(track: Track) {
        Log.d(TAG, "add track: $track")

        val tracksFromSearchHistory = read().toMutableList()
        tracksFromSearchHistory.removeIf { it.trackId == track.trackId }

        if (tracksFromSearchHistory.size >= MAX_HISTORY_SIZE) {
            tracksFromSearchHistory.removeAt(tracksFromSearchHistory.size - 1)
        }
        tracksFromSearchHistory.add(0, track)

        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, Gson().toJson(tracksFromSearchHistory))
            .apply()
    }

    fun clearHistory() {
        Log.d(TAG, "clear history")
        sharedPreferences.edit()
            .remove(SEARCH_HISTORY_KEY)
            .apply()
    }

    fun read() : List<Track> {
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