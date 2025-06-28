package org.guru.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import org.guru.playlistmaker.api.ItunesService
import org.guru.playlistmaker.data.SearchHistory
import org.guru.playlistmaker.data.Track
import org.guru.playlistmaker.data.TrackResponse
import org.guru.playlistmaker.trackAdapter.TrackAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Collections

class SearchActivity : AppCompatActivity() {

    private lateinit var clearBtn: ImageView
    private lateinit var searchEditTxt: EditText
    private lateinit var trackRecyclerView: RecyclerView
    private lateinit var tracksAdapter: TrackAdapter
    private lateinit var trackNotFoundLayout: LinearLayout
    private lateinit var notConnectionLayout: LinearLayout
    private lateinit var updateBtn: Button
    private lateinit var yourSearchTxtView: TextView
    private lateinit var clearHistoryBtn: Button

    private val itunesService = ItunesService()
    private lateinit var searchHistory: SearchHistory

    private var searchQuery = SEARCH_QUERY_DEF
    private val TAG = "SEARCH"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val app = applicationContext as App

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener { finish() }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchHintVisibility = searchEditTxt.hasFocus() && p0?.isEmpty() == true
                onSearchHistory(if(searchHintVisibility) View.VISIBLE else View.GONE)
            }

            override fun afterTextChanged(p0: Editable?) {
                searchQuery = p0.toString()
                val searchIsEmpty = searchQuery.isEmpty()
                clearBtn.visibility = if (searchIsEmpty) View.GONE else View.VISIBLE
            }

        }

        clearBtn = findViewById(R.id.cleatBtn)
        clearBtn.visibility = View.GONE
        clearBtn.setOnClickListener {
            searchEditTxt.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearBtn.windowToken, 0)
            setDefaultState()
        }

        searchEditTxt = findViewById(R.id.searchEditTxt)
        searchEditTxt.addTextChangedListener(simpleTextWatcher)
        searchEditTxt.setOnFocusChangeListener { view, hasFocus ->
            val visibility = if (hasFocus && searchEditTxt.text.isEmpty()) View.VISIBLE else View.GONE
            onSearchHistory(visibility)

        }
        searchEditTxt.setOnEditorActionListener{ _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                onSearchResponse()
                true
            }
            false
        }

        trackRecyclerView = findViewById(R.id.trackRecyclerView)
        tracksAdapter = TrackAdapter(
            Collections.emptyList(),
            onClick = {searchHistory.addTrack(it)}
        )
        trackRecyclerView.adapter = tracksAdapter

        trackNotFoundLayout = findViewById(R.id.track_not_found_layout)
        notConnectionLayout = findViewById(R.id.not_connection_layout)
        updateBtn = findViewById(R.id.updateBtn)
        updateBtn.setOnClickListener { onSearchResponse() }

        yourSearchTxtView = findViewById(R.id.yourSearchTxtView)
        clearHistoryBtn = findViewById(R.id.clearHistoryBtn)
        clearHistoryBtn.setOnClickListener{
            setDefaultState()
            searchHistory.clearHistory()
        }

        searchHistory = SearchHistory(app.sharedPrefs)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchEditTxt.setText(savedInstanceState.getString(SEARCH_QUERY))
    }

    private fun onSearchResponse() {
        val text = searchEditTxt.text
        if (text.isNotEmpty()) {
            itunesService.search(text.toString(), object : Callback<TrackResponse> {

                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {

                    Log.i(TAG, "onResponse $response")
                    if (response.isSuccessful) {

                        response.body()?.let {
                            if (it.resultCount.toInt() != 0) {
                                val tracks = response.body()!!.results
                                onSearchSuccessful(tracks)
                            } else {
                                onTracksNotFound()
                            }

                        } ?: onTracksNotFound()

                    } else{
                        onNotConnection()
                    }

                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    onNotConnection()
                    t.printStackTrace()
                }

            })
        }

    }

    private fun setDefaultState() {
        tracksAdapter.tracks = Collections.emptyList()
        searchEditTxt.clearFocus()
        yourSearchTxtView.visibility =  View.GONE
        clearHistoryBtn.visibility =  View.GONE
        trackRecyclerView.visibility = View.GONE
        trackNotFoundLayout.visibility = View.GONE
        notConnectionLayout.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onSearchSuccessful(list: List<Track>) {
        Log.i(TAG, "Set new track list: ${list.size}")
        tracksAdapter.tracks = list
        tracksAdapter.notifyDataSetChanged()
        trackRecyclerView.visibility = View.VISIBLE
        trackNotFoundLayout.visibility = View.GONE
        notConnectionLayout.visibility = View.GONE
    }

    private fun onTracksNotFound() {
        trackRecyclerView.visibility = View.GONE
        trackNotFoundLayout.visibility = View.VISIBLE
        notConnectionLayout.visibility = View.GONE
    }

    private fun onNotConnection() {
        trackRecyclerView.visibility = View.GONE
        trackNotFoundLayout.visibility = View.GONE
        notConnectionLayout.visibility = View.VISIBLE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onSearchHistory(visible: Int) {
        if (yourSearchTxtView.visibility == visible)
            return

        val tracks = searchHistory.read()

        if (visible == View.VISIBLE && tracks.isEmpty())
            return

        yourSearchTxtView.visibility =  visible
        clearHistoryBtn.visibility =  visible
        trackRecyclerView.visibility = visible

        tracksAdapter.tracks = tracks
        tracksAdapter.notifyDataSetChanged()
    }

    private companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
        const val SEARCH_QUERY_DEF = ""
    }

}