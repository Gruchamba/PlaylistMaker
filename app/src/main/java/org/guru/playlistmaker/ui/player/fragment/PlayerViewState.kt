package org.guru.playlistmaker.ui.player.fragment

import androidx.core.content.ContextCompat
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.FragmentPlayerBinding
import java.text.SimpleDateFormat
import java.util.Locale

sealed class PlayerViewState {

    protected val simpleDateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())

    class Play : PlayerViewState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.playButton.setImageResource(R.drawable.ic_stop_btn)
            binding.trackProgress.text = simpleDateFormat.format(0)
        }
    }

    class Playing(private val playerPosition: Int) : PlayerViewState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.playButton.setImageResource(R.drawable.ic_stop_btn)
            binding.trackProgress.text = simpleDateFormat.format(playerPosition)
        }
    }

    class Pause(private val playerPosition: Int) : PlayerViewState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.playButton.setImageResource(R.drawable.ic_play_btn)
            binding.trackProgress.text = simpleDateFormat.format(playerPosition)
        }

    }

    class Prepare : PlayerViewState() {
        override fun render(binding: FragmentPlayerBinding) {
            binding.playButton.setImageResource(R.drawable.ic_play_btn)
            binding.trackProgress.text =
                ContextCompat.getString(binding.trackProgress.context, R.string.def_track_progress)
        }

    }

    abstract fun render(binding: FragmentPlayerBinding)

}