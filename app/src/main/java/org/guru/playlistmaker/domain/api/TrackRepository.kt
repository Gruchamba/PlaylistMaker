package org.guru.playlistmaker.domain.api

import org.guru.playlistmaker.domain.models.Track

interface TrackRepository {
    fun searchMovies(expression: String): List<Track>
}