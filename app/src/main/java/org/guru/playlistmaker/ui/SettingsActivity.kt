package org.guru.playlistmaker.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.ActivitySettingsBinding
import org.guru.playlistmaker.presentation.settings.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private val TAG = SettingsActivity::class.java.name
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            SettingsViewModel.getFactory()
        )[SettingsViewModel::class.java]

        binding.apply {
            backBtn.setOnClickListener { finish() }

            themeSwitcher.apply {
                setOnCheckedChangeListener { _, checked -> viewModel.switchTheme(checked) }
            }

            viewModel.observeDarkTheme().observe(this@SettingsActivity) {
                themeSwitcher.isChecked = it
            }

            shareAppBtn.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {  }
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.uri_to_course))
                shareIntent.setType("text/plain")
                startActivity(shareIntent)

                val chooserIntent = Intent.createChooser(shareIntent, null)

                try {
                    startActivity(chooserIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this@SettingsActivity, getString(R.string.share_app_toast_message), Toast.LENGTH_SHORT).show()
                }
            }

            mailToSupportBtn.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SENDTO)
                shareIntent.data = Uri.parse("mailto:")
                shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.default_email)))
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.title_mail_for_support))
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.text_mail_for_support))
                startActivity(shareIntent)
            }

            userAgreementBtn.setOnClickListener {
                startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.uri_to_user_agreement)))
                )
            }
        }

    }
}