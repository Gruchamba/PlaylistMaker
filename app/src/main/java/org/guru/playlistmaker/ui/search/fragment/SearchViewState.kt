package org.guru.playlistmaker.ui.search.fragment

import android.view.View
import org.guru.playlistmaker.databinding.FragmentSearchBinding
import org.guru.playlistmaker.domain.search.model.Track

sealed class SearchViewState(val trackList: List<Track>? = null) {

    class Loading : SearchViewState() {
        override fun render(binding: FragmentSearchBinding) {
            binding.apply {
                yourSearchTxtView.visibility =  View.GONE
                progressBar.visibility = View.VISIBLE
                trackRecyclerView.visibility = View.GONE
                trackNotFoundLayout.visibility = View.GONE
                notConnectionLayout.visibility = View.GONE
                clearHistoryBtn.visibility = View.GONE
            }
        }

    }

    class Content(list: List<Track>) : SearchViewState(list) {
        override fun render(binding: FragmentSearchBinding) {
            binding.apply {
                yourSearchTxtView.visibility =  View.GONE
                trackRecyclerView.visibility = View.VISIBLE
                trackNotFoundLayout.visibility = View.GONE
                notConnectionLayout.visibility = View.GONE
                progressBar.visibility = View.GONE
                clearHistoryBtn.visibility = View.GONE
            }
        }
    }

    class ShowSearchHistory(val list: List<Track>) : SearchViewState(list) {
        override fun render(binding: FragmentSearchBinding) {
            binding.apply {
                val isVisible = if (list.isNotEmpty()) View.VISIBLE else View.GONE
                yourSearchTxtView.visibility = isVisible
                clearHistoryBtn.visibility =  isVisible
                trackRecyclerView.visibility = isVisible
            }
        }

    }

    class Empty : SearchViewState() {
        override fun render(binding: FragmentSearchBinding) {
            binding.apply {
                trackRecyclerView.visibility = View.GONE
                trackNotFoundLayout.visibility = View.VISIBLE
                notConnectionLayout.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
        }

    }

    class NotConnection : SearchViewState() {
        override fun render(binding: FragmentSearchBinding) {
            binding.apply {
                trackRecyclerView.visibility = View.GONE
                trackNotFoundLayout.visibility = View.GONE
                progressBar.visibility = View.GONE
                notConnectionLayout.visibility = View.VISIBLE
            }
        }
    }

    class DefaultState : SearchViewState() {
        override fun render(binding: FragmentSearchBinding) {
            binding.apply {
                searchEditTxt.clearFocus()
                yourSearchTxtView.visibility =  View.GONE
                clearHistoryBtn.visibility =  View.GONE
                trackRecyclerView.visibility = View.GONE
                trackNotFoundLayout.visibility = View.GONE
                notConnectionLayout.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
        }

    }

    class Error : SearchViewState() {
        override fun render(binding: FragmentSearchBinding) {

        }

    }

    abstract fun render(binding: FragmentSearchBinding)
}