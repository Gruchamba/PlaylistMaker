package org.guru.playlistmaker.domain.library.playlist.model

import java.io.Serializable

data class Playlist(
    val playlistId: Int,
    val title: String,
    val description: String?,
    val uriImage: String?,
    var tracksIdList: MutableList<String>,
    var size: Int
) : Serializable