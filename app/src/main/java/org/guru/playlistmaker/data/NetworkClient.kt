package org.guru.playlistmaker.data

import org.guru.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}