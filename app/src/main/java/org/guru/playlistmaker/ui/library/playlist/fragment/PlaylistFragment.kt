package org.guru.playlistmaker.ui.library.playlist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.guru.playlistmaker.R
import org.guru.playlistmaker.ui.library.favorites.view_model.FavoritesTrackViewModel
import org.guru.playlistmaker.ui.library.playlist.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

class PlaylistFragment : Fragment() {

    private val playlistViewModel: PlaylistViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false)
    }

}