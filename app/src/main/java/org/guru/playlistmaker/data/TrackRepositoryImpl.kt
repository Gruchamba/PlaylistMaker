package org.guru.playlistmaker.data

import org.guru.playlistmaker.data.dto.TrackSearchRequest
import org.guru.playlistmaker.data.dto.TrackSearchResponse
import org.guru.playlistmaker.domain.api.TrackRepository
import org.guru.playlistmaker.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchMovies(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as TrackSearchResponse).results.map {
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
            }
        } else {
            return emptyList()
        }
    }
}