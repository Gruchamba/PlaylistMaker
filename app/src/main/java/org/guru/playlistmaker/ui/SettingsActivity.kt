package org.guru.playlistmaker.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.guru.playlistmaker.Creator
import org.guru.playlistmaker.R
import org.guru.playlistmaker.domain.api.ConfigurationInteractor

class SettingsActivity : AppCompatActivity() {

    private val configurationInteractor: ConfigurationInteractor by lazy {
        Creator.getConfigurationAppInteractor()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener { finish() }
        
        val themeSwitcher = findViewById<Switch>(R.id.themeSwitcher)
        themeSwitcher.isChecked = configurationInteractor.isDarkTheme()
        themeSwitcher.setOnCheckedChangeListener { _, checked -> configurationInteractor.switchTheme(checked) }

        val shareAppBtn = findViewById<ImageView>(R.id.shareAppBtn)
        shareAppBtn.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.uri_to_course))
            shareIntent.setType("text/plain")
            startActivity(shareIntent)

            val chooserIntent = Intent.createChooser(shareIntent, null)

            try {
                startActivity(chooserIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, getString(R.string.share_app_toast_message), Toast.LENGTH_SHORT).show()
            }
        }

        val mailToSupportBtn = findViewById<ImageView>(R.id.mailToSupportBtn)
        mailToSupportBtn.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.default_email)))
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