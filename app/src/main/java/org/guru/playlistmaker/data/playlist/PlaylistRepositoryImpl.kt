package org.guru.playlistmaker.data.playlist

import org.guru.playlistmaker.data.db.converters.PlaylistDbConverter
import org.guru.playlistmaker.data.db.dao.PlaylistDao
import org.guru.playlistmaker.data.db.entity.PlaylistEntity
import org.guru.playlistmaker.domain.library.playlist.PlaylistRepository
import org.guru.playlistmaker.domain.library.playlist.model.Playlist

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistDbConverter: PlaylistDbConverter
) : PlaylistRepository {

    override suspend fun createPlaylist(playlist: Playlist) {
        playlistDao.createPlaylist(playlistDbConverter.map(playlist))
    }

    override suspend fun getAllPlaylists() : List<Playlist> {
        return convertFromPlaylistEntity(
            playlistDao.getAllPlaylists()
        )
    }

    private fun convertFromPlaylistEntity(playlistEntityList: List<PlaylistEntity>) : List<Playlist> {
        return playlistEntityList.map { playlist -> playlistDbConverter.map(playlist) }
    }
}