package org.guru.playlistmaker.data.playlist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.guru.playlistmaker.data.db.converters.PlaylistDbConverter
import org.guru.playlistmaker.data.db.converters.TrackForPlaylistDbConverter
import org.guru.playlistmaker.data.db.dao.PlaylistDao
import org.guru.playlistmaker.data.db.dao.TrackForPlaylistDao
import org.guru.playlistmaker.data.db.entity.PlaylistEntity
import org.guru.playlistmaker.data.db.entity.TrackForPlaylistEntity
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

    override suspend fun getPlaylistById(playlistId: Int): Flow<Playlist> = flow {
        emit(
            playlistDbConverter.map(
                playlistDao.getPlaylistById(playlistId)
            )
        )
    }

    override suspend fun getTracksForPlaylist(tracksId: List<String>): Flow<List<Track>> = flow {
        emit(
            convertFromTrackForPlaylistEntity(
                trackForPlaylistDao.getTracksByIds(tracksId)
            )
        )
    }

    override suspend fun removeTrackFromPlaylist(
        playlist: Playlist,
        tracks: List<Track>,
        trackId: String
    ): Flow<List<Track>> = flow {

        val track = trackForPlaylistDbConverter.map(
            trackForPlaylistDao.getTracksByIds(trackId)
        )

        playlist.size--
        playlist.tracksIdList.remove(trackId)

        playlistDao.updatePlaylist(playlistDbConverter.map(playlist))

        if (!checkTrackForPlaylist(trackId)) {
            trackForPlaylistDao.deleteTrack(
                trackForPlaylistDbConverter.map(track)
            )
        }

        val mutableList = tracks.toMutableList()
        mutableList.remove(track)

        emit(mutableList)

    }

    override suspend fun removePlaylist(playlist: Playlist) : Flow<Boolean> = flow {
        playlistDao.deletePlaylist(playlistDbConverter.map(playlist))
        playlist.tracksIdList.forEach {
            if (!checkTrackForPlaylist(it)) {
                trackForPlaylistDao.deleteTrackById(
                    it
                )
            }
        }

        emit(true)
    }

    private suspend fun checkTrackForPlaylist(trackId: String) : Boolean {
        return convertFromPlaylistEntity(
            playlistDao.getAllPlaylists()
        ).any { playlist -> playlist.tracksIdList.contains(trackId) }
    }

    private fun convertFromPlaylistEntity(entityList: List<PlaylistEntity>) : List<Playlist> {
        return entityList.map { playlist -> playlistDbConverter.map(playlist) }
    }

    private fun convertFromTrackForPlaylistEntity(entityList: List<TrackForPlaylistEntity>) : List<Track> {
        return entityList.map { playlist -> trackForPlaylistDbConverter.map(playlist) }
    }
}