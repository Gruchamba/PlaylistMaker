package org.guru.playlistmaker.domain.library.playlist.impl

import kotlinx.coroutines.flow.Flow
import org.guru.playlistmaker.domain.library.playlist.PlaylistInteractor
import org.guru.playlistmaker.domain.library.playlist.PlaylistRepository
import org.guru.playlistmaker.domain.library.playlist.model.Playlist
import org.guru.playlistmaker.domain.search.model.Track

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor{

    override suspend fun createPlaylist(title: String, description: String?, imageUri: String?) {
        playlistRepository.createPlaylist(title, description, imageUri)
    }

    override suspend fun getAllPlaylists() : Flow<List<Playlist>> {
        return playlistRepository.getAllPlaylists()
    }

    override suspend fun addTrackForPlaylist(playlist: Playlist, track: Track) : Flow<Playlist> {
        return playlistRepository.addTrackForPlaylist(playlist, track)
    }
}