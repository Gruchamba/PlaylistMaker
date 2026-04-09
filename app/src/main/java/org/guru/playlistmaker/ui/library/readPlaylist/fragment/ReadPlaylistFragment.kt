package org.guru.playlistmaker.ui.library.readPlaylist.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.guru.playlistmaker.databinding.FragmentReadPlaylistBinding
import org.guru.playlistmaker.ui.library.readPlaylist.view_model.ReadPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReadPlaylistFragment : Fragment() {

    private var _binding: FragmentReadPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReadPlaylistViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReadPlaylistBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}