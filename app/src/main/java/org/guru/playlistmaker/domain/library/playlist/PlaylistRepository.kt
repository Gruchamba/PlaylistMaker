package org.guru.playlistmaker.domain.library.playlist

import org.guru.playlistmaker.domain.library.playlist.model.Playlist

interface PlaylistRepository {

    suspend fun createPlaylist(title: String, description: String?, imageUri: String?)

    suspend fun getAllPlaylists() : List<Playlist>
}