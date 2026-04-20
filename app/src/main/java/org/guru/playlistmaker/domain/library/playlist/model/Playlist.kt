package org.guru.playlistmaker.domain.library.playlist.model

import java.io.Serializable

data class Playlist(
    val playlistId: Int,
    var title: String,
    var description: String?,
    var uriImage: String?,
    var tracksIdList: MutableList<String>,
    var size: Int
) : Serializable