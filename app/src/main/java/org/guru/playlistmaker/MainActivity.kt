package org.guru.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBtn = findViewById<Button>(R.id.searchBtn)
        val searchBtnClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(this@MainActivity, ActivityFind::class.java))
            }
        }

        searchBtn.setOnClickListener(searchBtnClickListener)

        val libraryBtn = findViewById<Button>(R.id.libraryBtn)
        libraryBtn.setOnClickListener{
            startActivity(Intent(this@MainActivity, ActivityMediaLibrary::class.java))
        }

        val settingsBtn = findViewById<Button>(R.id.settingsBtn)
        settingsBtn.setOnClickListener{
            startActivity(Intent(this@MainActivity, ActivitySettings::class.java))
        }
    }
}