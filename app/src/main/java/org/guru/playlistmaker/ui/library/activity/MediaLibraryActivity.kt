package org.guru.playlistmaker.ui.library.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.ActivityMediaLibraryBinding
import org.guru.playlistmaker.ui.library.activity.pagerAdapter.MediaLibraryPagerAdapter

class MediaLibraryActivity : AppCompatActivity() {

    private var _binding: ActivityMediaLibraryBinding? = null
    private val binding get() = _binding!!

    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMediaLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            backBtn.setOnClickListener { finish() }

            viewPager.adapter = MediaLibraryPagerAdapter(supportFragmentManager, lifecycle)
            tabMediator = TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when(position) {
                    0 -> tab.text = getString(R.string.favorites_tracks)
                    else -> tab.text = getString(R.string.playlists)
                }
            }
            tabMediator.attach()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}