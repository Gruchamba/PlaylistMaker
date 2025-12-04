package org.guru.playlistmaker.data.search

import org.guru.playlistmaker.data.NetworkClient
import org.guru.playlistmaker.data.search.dto.Response
import org.guru.playlistmaker.data.search.dto.TrackSearchRequest
import org.guru.playlistmaker.data.search.dto.TrackSearchResponse
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Callback

class RetrofitNetworkClient : NetworkClient, KoinComponent {

    private val itunesApiService: ItunesApiService by inject()

    fun search(query: String, callback: Callback<TrackSearchResponse>) {
        itunesApiService.searchTrack(query).enqueue(callback)
    }

    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val resp = itunesApiService.searchTrack(dto.expression).execute()

            val body = resp.body() ?: Response()

            return body.apply { resultCode = resp.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }

}