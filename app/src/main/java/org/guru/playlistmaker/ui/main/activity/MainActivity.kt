package org.guru.playlistmaker.ui.main.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.guru.playlistmaker.databinding.ActivityMainBinding
import org.guru.playlistmaker.ui.library.activity.MediaLibraryActivity
import org.guru.playlistmaker.ui.search.activity.SearchActivity
import org.guru.playlistmaker.ui.settings.activity.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            searchBtn.setOnClickListener {
                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
            }

            libraryBtn.setOnClickListener{
                startActivity(Intent(this@MainActivity, MediaLibraryActivity::class.java))
            }

            settingsBtn.setOnClickListener{
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
            }
        }

    }
}