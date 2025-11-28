package org.guru.playlistmaker.data

import org.guru.playlistmaker.data.search.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}