package org.guru.playlistmaker.api

import org.guru.playlistmaker.data.TrackResponse
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class ItunesService {

    private val searchBaseUrl = "https://itunes.apple.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(searchBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesApiService = retrofit.create<ItunesApiService>()

    fun search(query: String, callback: Callback<TrackResponse>) {
        itunesApiService.search(query).enqueue(callback)
    }

}