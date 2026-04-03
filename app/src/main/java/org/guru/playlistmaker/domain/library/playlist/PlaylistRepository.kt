package org.guru.playlistmaker.domain.library.playlist

import org.guru.playlistmaker.domain.library.playlist.model.Playlist

interface PlaylistRepository {

    suspend fun createPlaylist(playlist: Playlist)

    suspend fun getAllPlaylists() : List<Playlist>
}