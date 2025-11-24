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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import org.guru.playlistmaker.R
import org.guru.playlistmaker.creator.Creator
import org.guru.playlistmaker.databinding.ActivityMainBinding
import org.guru.playlistmaker.databinding.ActivitySearchBinding
import org.guru.playlistmaker.domain.api.TrackInteractor
import org.guru.playlistmaker.domain.models.Track
import org.guru.playlistmaker.ui.AudioPlayerActivity.Companion.TRACK_KEY
import org.guru.playlistmaker.ui.trackAdapter.TrackAdapter
import java.util.Collections

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val trackInteractor: TrackInteractor by lazy { Creator.provideTracksInteractor() }
    private lateinit var tracksAdapter: TrackAdapter

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable { onSearchResponse() }
    private var searchQuery = SEARCH_QUERY_DEF
    private val TAG = "SEARCH"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            backBtn.setOnClickListener { finish() }

            val simpleTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val searchHintVisibility = searchEditTxt.hasFocus() && p0?.isEmpty() == true
                    onSearchHistory(if(searchHintVisibility) View.VISIBLE else View.GONE)
                    if (!p0.isNullOrEmpty()) searchDebounce()
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
                    setDefaultState()
                }
            }
            searchEditTxt.apply {
                addTextChangedListener(simpleTextWatcher)
                setOnFocusChangeListener { view, hasFocus ->
                    val visibility = if (hasFocus && searchEditTxt.text.isEmpty()) View.VISIBLE else View.GONE
                    onSearchHistory(visibility)

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
                        trackInteractor.addTrackToHistory(it)
                        val intent = Intent(this@SearchActivity, AudioPlayerActivity::class.java)
                        intent.putExtra(TRACK_KEY, it)
                        startActivity(intent)
                    }
                }
            )
            trackRecyclerView.adapter = tracksAdapter

            updateBtn.setOnClickListener { onSearchResponse() }

            clearHistoryBtn.setOnClickListener{
                setDefaultState()
                trackInteractor.clearTracksHistory()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.searchEditTxt.setText(savedInstanceState.getString(SEARCH_QUERY))
    }

    private fun onSearchResponse() {
        val text = binding.searchEditTxt.text
        if (text.isNotEmpty()) {
            onSearchStart()

            trackInteractor.searchTracks(text.toString(), object: TrackInteractor.TrackConsumer {
                override fun consume(foundTracks: List<Track>) {
                    handler.post {
                        if (foundTracks.isNotEmpty()) {
                            onSearchSuccessful(foundTracks)
                        } else {
                            onTracksNotFound()
                        }
                    }
                }
            })

        }

    }

    private fun setDefaultState() {
        tracksAdapter.tracks = Collections.emptyList()
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

    @SuppressLint("NotifyDataSetChanged")
    private fun onSearchSuccessful(list: List<Track>) {
        Log.i(TAG, "Set new track list: ${list.size}")
        tracksAdapter.tracks = list
        tracksAdapter.notifyDataSetChanged()
        binding.apply {
            trackRecyclerView.visibility = View.VISIBLE
            trackNotFoundLayout.visibility = View.GONE
            notConnectionLayout.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
    }

    private fun onTracksNotFound() {
        binding.apply {
            trackRecyclerView.visibility = View.GONE
            trackNotFoundLayout.visibility = View.VISIBLE
            notConnectionLayout.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
    }

    private fun onNotConnection() {
        binding.apply {
            trackRecyclerView.visibility = View.GONE
            trackNotFoundLayout.visibility = View.GONE
            progressBar.visibility = View.GONE
            notConnectionLayout.visibility = View.VISIBLE
        }
    }

    private fun onSearchStart() {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            trackRecyclerView.visibility = View.GONE
            trackNotFoundLayout.visibility = View.GONE
            notConnectionLayout.visibility = View.GONE
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onSearchHistory(visible: Int) {
        if (binding.yourSearchTxtView.visibility == visible)
            return

        val tracks = trackInteractor.readTracksFromHistory()

        if (visible == View.VISIBLE && tracks.isEmpty())
            return

        binding.apply {
            yourSearchTxtView.visibility =  visible
            clearHistoryBtn.visibility =  visible
            trackRecyclerView.visibility = visible
        }

        tracksAdapter.tracks = tracks
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

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
        const val SEARCH_QUERY_DEF = ""
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}