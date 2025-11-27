package org.guru.playlistmaker.data

import org.guru.playlistmaker.data.dto.TrackSearchRequest
import org.guru.playlistmaker.data.dto.TrackSearchResponse
import org.guru.playlistmaker.domain.api.TrackRepository
import org.guru.playlistmaker.domain.models.Track
import org.guru.playlistmaker.util.Resource

class TrackRepositoryImpl(private val tracksHistoryStorage: TracksHistoryStorage, private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when(response.resultCode) {
            -1 -> Resource.Error("Проверьте подключение к интернету")
            200 -> Resource.Success((response as TrackSearchResponse).results.map {
                Track(
                    it.trackId,
                    it.trackName,
                    it.artistName,
                    it.collectionName,
                    it.releaseDate,
                    it.primaryGenreName,
                    it.country,
                    it.getFormatTrackTime(),
                    it.artworkUrl100,
                    it.previewUrl)
            })
            else -> Resource.Error("Ошибка сервера")
        }
    }

    override fun addTrackToHistory(track: Track) {
        tracksHistoryStorage.addTrackToHistory(track)
    }

    override fun clearTracksHistory() {
        tracksHistoryStorage.clearTracksHistory()
    }

    override fun readTracksFromHistory(): List<Track> {
        return tracksHistoryStorage.readTracksFromHistory()
    }
}