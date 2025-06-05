package org.guru.playlistmaker

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        val darkThemeSwitch = findViewById<Switch>(R.id.darkThemeSwitch)

        val shareAppBtn = findViewById<ImageView>(R.id.shareAppBtn)
        shareAppBtn.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.uri_to_course))
            shareIntent.setType("text/plain")
            startActivity(shareIntent)

            // Создаем диалог выбора мессенджера
            val chooserIntent = Intent.createChooser(shareIntent, null)

            try {
                startActivity(chooserIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Нет доступных приложений для sharing", Toast.LENGTH_SHORT).show()
            }
        }

        val mailToSupportBtn = findViewById<ImageView>(R.id.mailToSupportBtn)
        mailToSupportBtn.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, getString(R.string.default_email))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.title_mail_for_support))
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.text_mail_for_support))
            startActivity(shareIntent)
        }

        val userAgreementBtn = findViewById<ImageView>(R.id.userAgreementBtn)
        userAgreementBtn.setOnClickListener {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.uri_to_user_agreement)))
            )
        }

    }
}