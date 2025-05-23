package org.guru.playlistmaker

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
                Toast.makeText(this@MainActivity, "Поиск!", Toast.LENGTH_SHORT).show()
            }
        }

        searchBtn.setOnClickListener(searchBtnClickListener)

        val libraryBtn = findViewById<Button>(R.id.libraryBtn)
        libraryBtn.setOnClickListener{
            Toast.makeText(this@MainActivity, "Медиатека", Toast.LENGTH_SHORT).show()
        }

        val settingsBtn = findViewById<Button>(R.id.settingsBtn)
        settingsBtn.setOnClickListener{
            Toast.makeText(this@MainActivity, "Настройки", Toast.LENGTH_SHORT).show()
        }
    }
}