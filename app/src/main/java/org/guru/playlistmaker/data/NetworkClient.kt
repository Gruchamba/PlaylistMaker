package org.guru.playlistmaker.data

import org.guru.playlistmaker.data.search.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}