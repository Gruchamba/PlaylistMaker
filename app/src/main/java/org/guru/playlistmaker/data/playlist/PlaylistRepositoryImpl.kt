package org.guru.playlistmaker.data.playlist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.guru.playlistmaker.data.db.converters.PlaylistDbConverter
import org.guru.playlistmaker.data.db.converters.TrackForPlaylistDbConverter
import org.guru.playlistmaker.data.db.dao.PlaylistDao
import org.guru.playlistmaker.data.db.dao.TrackForPlaylistDao
import org.guru.playlistmaker.data.db.entity.PlaylistEntity
import org.guru.playlistmaker.domain.library.playlist.PlaylistRepository
import org.guru.playlistmaker.domain.library.playlist.model.Playlist
import org.guru.playlistmaker.domain.search.model.Track

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistDbConverter: PlaylistDbConverter,
    private val trackForPlaylistDao: TrackForPlaylistDao,
    private val trackForPlaylistDbConverter: TrackForPlaylistDbConverter
) : PlaylistRepository {

    override suspend fun createPlaylist(title: String, description: String?, imageUri: String?) {
        playlistDao.createPlaylist(
            playlistDbConverter.map(
                Playlist(
                    playlistId = 0,
                    title = title,
                    description = description,
                    uriImage = imageUri,
                    tracksIdList = mutableListOf(),
                    size = 0
                )
            )
        )
    }

    override suspend fun getAllPlaylists() : Flow<List<Playlist>> = flow {
        emit(
            convertFromPlaylistEntity(
                playlistDao.getAllPlaylists()
            )
        )

    }

    override suspend fun addTrackForPlaylist(playlist: Playlist, track: Track) : Flow<Playlist> = flow {
        trackForPlaylistDao.addTrackForPlaylist(trackForPlaylistDbConverter.map(track))

        playlist.size++
        playlist.tracksIdList.add(track.trackId!!)

        playlistDao.updatePlaylist(playlistDbConverter.map(playlist))

        emit(playlist)

    }

    private fun convertFromPlaylistEntity(playlistEntityList: List<PlaylistEntity>) : List<Playlist> {
        return playlistEntityList.map { playlist -> playlistDbConverter.map(playlist) }
    }
}