package org.guru.playlistmaker.api

import org.guru.playlistmaker.data.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface  ItunesApiService {

    @GET("search")
    fun search(@Query("term") text: String) : Call<TrackResponse>

}