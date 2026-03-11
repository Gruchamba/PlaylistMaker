package org.guru.playlistmaker.data.search

import org.guru.playlistmaker.data.NetworkClient
import org.guru.playlistmaker.data.search.dto.Response
import org.guru.playlistmaker.data.search.dto.TrackSearchRequest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RetrofitNetworkClient : NetworkClient, KoinComponent {

    private val itunesApiService: ItunesApiService by inject()

    override suspend fun doRequest(dto: Any): Response {

        if (dto !is TrackSearchRequest) {
            return Response().apply { resultCode = 400 }
        }

        return try {
            val response = itunesApiService.searchTrack(dto.expression)
            response.apply { resultCode = 200 }
        } catch (_: Throwable) {
            Response().apply { resultCode = 500 }
        }

    }

}