package org.guru.playlistmaker.data.search.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.guru.playlistmaker.data.NetworkClient
import org.guru.playlistmaker.data.search.dto.TrackSearchRequest
import org.guru.playlistmaker.data.search.dto.TrackSearchResponse
import org.guru.playlistmaker.data.search.storage.TracksHistoryStorage
import org.guru.playlistmaker.domain.search.Resource
import org.guru.playlistmaker.domain.search.SearchTrackRepository
import org.guru.playlistmaker.domain.search.model.Track

class SearchTrackRepositoryImpl(private val tracksHistoryStorage: TracksHistoryStorage, private val networkClient: NetworkClient) :
    SearchTrackRepository {

    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when(response.resultCode) {
            -1 -> { emit(Resource.Error("Проверьте подключение к интернету")) }
            200 -> {
                emit(
                    Resource.Success((response as TrackSearchResponse).results.map {
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
                )

            }
            else -> emit(Resource.Error("Ошибка сервера"))
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