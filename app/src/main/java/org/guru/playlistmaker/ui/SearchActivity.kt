package org.guru.playlistmaker.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import org.guru.playlistmaker.databinding.ActivitySearchBinding
import org.guru.playlistmaker.domain.models.Track
import org.guru.playlistmaker.presentation.search.SearchViewModel
import org.guru.playlistmaker.presentation.search.SearchViewState
import org.guru.playlistmaker.ui.AudioPlayerActivity.Companion.TRACK_KEY
import org.guru.playlistmaker.ui.trackAdapter.TrackAdapter
import java.util.Collections

class SearchActivity : AppCompatActivity() {

    val TAG = SearchActivity::class.java.name
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var tracksAdapter: TrackAdapter

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private var searchQuery = SEARCH_QUERY_DEF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this@SearchActivity,
            SearchViewModel.getFactory()
        )[SearchViewModel::class.java]

        viewModel.observeState().observe(this) { render(it) }
        viewModel.observeShowToast().observe(this) { showToast(it) }

        binding.apply {
            backBtn.setOnClickListener { finish() }

            val simpleTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val searchHintVisibility = searchEditTxt.hasFocus() && p0?.isEmpty() == true
                    Log.i(TAG, "focus ${searchEditTxt.hasFocus()} p0 ${p0?.isEmpty()}")
                    if (searchHintVisibility) viewModel.readTracksFromHistory()
                    else if (!p0.isNullOrEmpty()) viewModel.searchDebounce(p0.toString())
                }

                override fun afterTextChanged(p0: Editable?) {
                    searchQuery = p0.toString()
                    val searchIsEmpty = searchQuery.isEmpty()
                    clearBtn.visibility = if (searchIsEmpty) View.GONE else View.VISIBLE
                }

            }

            clearBtn.apply {
                visibility = View.GONE
                setOnClickListener {
                    searchEditTxt.setText("")
                    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    inputMethodManager?.hideSoftInputFromWindow(clearBtn.windowToken, 0)
                    viewModel.setDefaultState()
                }
            }

            searchEditTxt.clearFocus()
            searchEditTxt.apply {
                addTextChangedListener(simpleTextWatcher)
                setOnFocusChangeListener { view, hasFocus ->
                    if(hasFocus && searchEditTxt.text.isNotEmpty())
                        viewModel.searchDebounce(searchEditTxt.text.toString())
                    else viewModel.readTracksFromHistory()

                }
                setOnEditorActionListener{ _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        onSearchResponse()
                        true
                    }
                    false
                }
            }

            tracksAdapter = TrackAdapter(
                Collections.emptyList(),
                onClick = {
                    if (clickDebounce() && !it.trackId.isNullOrEmpty()) {
                        viewModel.addTrackToHistory(it)
                        val intent = Intent(this@SearchActivity, AudioPlayerActivity::class.java)
                        intent.putExtra(TRACK_KEY, it)
                        startActivity(intent)
                    }
                }
            )

            trackRecyclerView.adapter = tracksAdapter
            updateBtn.setOnClickListener { onSearchResponse() }
            clearHistoryBtn.setOnClickListener{
                viewModel.setDefaultState()
                viewModel.clearTracksHistory()
            }
        }

    }

    private fun render(state: SearchViewState) {

        state.render(binding)

        if (state is SearchViewState.DefaultState)
            setDefaultState()

        if (state is SearchViewState.Content)
            state.trackList?.let { onSearchSuccessful(it) }

        if (state is SearchViewState.ShowSearchHistory)
            state.trackList?.let { onSearchHistory(it) }

    }

    private fun onSearchResponse() {
        Log.i(TAG, "send search request: ${binding.searchEditTxt.text}")
        viewModel.searchRequest(binding.searchEditTxt.text.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.searchEditTxt.setText(savedInstanceState.getString(SEARCH_QUERY))
        Log.i(TAG, "Restore instance state ${savedInstanceState.getString(SEARCH_QUERY)}")
    }

    private fun setDefaultState() {
        Log.i(TAG, "set default state")
        tracksAdapter.tracks = Collections.emptyList()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onSearchSuccessful(list: List<Track>) {
        Log.i(TAG, "Set new track list: ${list.size}")
        tracksAdapter.tracks = list
        tracksAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onSearchHistory(list: List<Track>) {
        Log.i(TAG, "on search history")
        tracksAdapter.tracks = list
        tracksAdapter.notifyDataSetChanged()
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
        const val SEARCH_QUERY_DEF = ""
        const val CLICK_DEBOUNCE_DELAY = 1000L

    }

}