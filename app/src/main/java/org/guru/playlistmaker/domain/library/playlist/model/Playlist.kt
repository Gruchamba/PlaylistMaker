package org.guru.playlistmaker.domain.library.playlist.model

import org.guru.playlistmaker.domain.search.model.Track

data class Playlist(
    val playlistId: Int,
    val title: String,
    val description: String?,
    val uriImage: String?,
    val tracks: List<Track>,
    val size: Int
)