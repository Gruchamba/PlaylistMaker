package org.guru.playlistmaker.ui.library.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.FragmentMediaLibraryBinding
import org.guru.playlistmaker.ui.library.fragment.pagerAdapter.MediaLibraryPagerAdapter

class MediaLibraryFragment : Fragment() {

    private var _binding: FragmentMediaLibraryBinding? = null
    private val binding get() = _binding!!

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaLibraryBinding.inflate(layoutInflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            viewPager.adapter = MediaLibraryPagerAdapter(childFragmentManager, lifecycle)
            tabMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when(position) {
                    0 -> tab.text = getString(R.string.favorites_tracks)
                    else -> tab.text = getString(R.string.playlists)
                }
            }
            tabMediator.attach()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(tabMediator.isAttached)
            tabMediator.detach()

        _binding = null
    }

}