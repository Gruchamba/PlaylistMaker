package org.guru.playlistmaker.domain.library.playlist.impl

import org.guru.playlistmaker.domain.library.playlist.PlaylistInteractor
import org.guru.playlistmaker.domain.library.playlist.PlaylistRepository
import org.guru.playlistmaker.domain.library.playlist.model.Playlist

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor{

    override suspend fun createPlaylist(title: String, description: String?, imageUri: String?) {
        playlistRepository.createPlaylist(title, description, imageUri)
    }

    override suspend fun getAllPlaylists() : List<Playlist> {
        return playlistRepository.getAllPlaylists()
    }
}