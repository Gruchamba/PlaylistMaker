package org.guru.playlistmaker.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.guru.playlistmaker.databinding.ActivityMainBinding

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