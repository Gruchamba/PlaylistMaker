package org.guru.playlistmaker.domain.library.playlist

import kotlinx.coroutines.flow.Flow
import org.guru.playlistmaker.domain.library.playlist.model.Playlist
import org.guru.playlistmaker.domain.search.model.Track

interface PlaylistRepository {

    suspend fun createPlaylist(title: String, description: String?, imageUri: String?)

    suspend fun getAllPlaylists() : Flow<List<Playlist>>

    suspend fun addTrackForPlaylist(playlist: Playlist, track: Track) : Flow<Playlist>
}