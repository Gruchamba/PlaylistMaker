package org.guru.playlistmaker.ui.library.newPlaylist.fragment

import org.guru.playlistmaker.domain.library.playlist.model.Playlist

sealed interface CreateOrUpdateStateView {

    data object CreateState : CreateOrUpdateStateView

    data class UpdateState(val playlist: Playlist) : CreateOrUpdateStateView
}