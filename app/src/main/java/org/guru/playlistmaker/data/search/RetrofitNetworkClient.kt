package org.guru.playlistmaker.data.search

import org.guru.playlistmaker.data.NetworkClient
import org.guru.playlistmaker.data.search.dto.Response
import org.guru.playlistmaker.data.search.dto.TrackSearchRequest
import org.guru.playlistmaker.data.search.dto.TrackSearchResponse
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitNetworkClient : NetworkClient {

    private val searchBaseUrl = "https://itunes.apple.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(searchBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesApiService = retrofit.create<ItunesApiService>()

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