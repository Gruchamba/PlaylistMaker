package org.guru.playlistmaker.data.network

import org.guru.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface  ItunesApiService {

    @GET("search")
    fun searchTrack(@Query("term") text: String) : Call<TrackSearchResponse>

}