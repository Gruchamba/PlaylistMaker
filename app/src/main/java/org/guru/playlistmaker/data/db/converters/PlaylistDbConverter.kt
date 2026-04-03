package org.guru.playlistmaker.data.db.converters

import com.google.gson.Gson
import org.guru.playlistmaker.data.db.entity.PlaylistEntity
import org.guru.playlistmaker.domain.library.playlist.model.Playlist
import org.guru.playlistmaker.domain.search.model.Track

class PlaylistDbConverter {

    fun map(playlist: Playlist) : PlaylistEntity {
        return PlaylistEntity(
            title = playlist.title,
            description = playlist.description,
            uriImage = playlist.uriImage,
            tracks = Gson().toJson(playlist.tracks),
            size = playlist.size
        )
    }

    fun map(playlistEntity: PlaylistEntity) : Playlist {
        return Playlist(
            playlistEntity.id,
            playlistEntity.title,
            playlistEntity.description,
            playlistEntity.uriImage,
            ArrayList(Gson().fromJson(playlistEntity.tracks, Array<Track>::class.java).toList()),
            playlistEntity.size
        )
    }
}