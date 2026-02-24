package org.guru.playlistmaker.ui.settings.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.FragmentSettingsBinding
import org.guru.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            themeSwitcher.apply {
                setOnCheckedChangeListener { _, checked ->
                    viewModel.switchTheme(checked)
                }
            }

            viewModel.observeDarkTheme().observe(viewLifecycleOwner) {
                themeSwitcher.isChecked = it
            }

            shareAppBtn.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {  }
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.uri_to_course))
                shareIntent.type = "text/plain"
                startActivity(shareIntent)

                val chooserIntent = Intent.createChooser(shareIntent, null)

                try {
                    startActivity(chooserIntent)
                } catch (_: ActivityNotFoundException) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.share_app_toast_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            mailToSupportBtn.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SENDTO)
                shareIntent.data = "mailto:".toUri()
                shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.default_email)))
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.title_mail_for_support))
                shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.text_mail_for_support))
                startActivity(shareIntent)
            }

            userAgreementBtn.setOnClickListener {
                startActivity(
                    Intent(Intent.ACTION_VIEW, getString(R.string.uri_to_user_agreement).toUri())
                )
            }
        }
    }
}