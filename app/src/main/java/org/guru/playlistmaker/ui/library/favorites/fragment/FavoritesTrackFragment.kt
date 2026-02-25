package org.guru.playlistmaker.ui.library.favorites.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.guru.playlistmaker.databinding.FragmentFavoritesTrackBinding
import org.guru.playlistmaker.ui.library.favorites.view_model.FavoritesTrackViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesTrackFragment : Fragment() {

    private var _binding: FragmentFavoritesTrackBinding? = null
    private val binding get() = _binding!!

    private val favoritesTrackViewModel: FavoritesTrackViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesTrackBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}