package org.guru.playlistmaker.data.search

import org.guru.playlistmaker.data.search.dto.TrackSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApiService {

    @GET("search?media=music")
    suspend fun searchTrack(@Query("term") text: String) : TrackSearchResponse

}