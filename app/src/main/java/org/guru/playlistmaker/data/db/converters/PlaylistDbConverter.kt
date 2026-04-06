package org.guru.playlistmaker.data.db.converters

import com.google.gson.Gson
import org.guru.playlistmaker.data.db.entity.PlaylistEntity
import org.guru.playlistmaker.domain.library.playlist.model.Playlist

class PlaylistDbConverter {

    fun map(playlist: Playlist) : PlaylistEntity {
        return PlaylistEntity(
            id = playlist.playlistId,
            title = playlist.title,
            description = playlist.description,
            uriImage = playlist.uriImage,
            tracksId = Gson().toJson(playlist.tracksIdList),
            size = playlist.size
        )
    }

    fun map(playlistEntity: PlaylistEntity) : Playlist {
        return Playlist(
            playlistEntity.id,
            playlistEntity.title,
            playlistEntity.description,
            playlistEntity.uriImage,
            ArrayList(Gson().fromJson(playlistEntity.tracksId, Array<String>::class.java).toMutableList()),
            playlistEntity.size
        )
    }
}