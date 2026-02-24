package org.guru.playlistmaker.ui.search.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.guru.playlistmaker.R
import org.guru.playlistmaker.databinding.FragmentSearchBinding
import org.guru.playlistmaker.domain.search.model.Track
import org.guru.playlistmaker.ui.player.fragment.PlayerFragment
import org.guru.playlistmaker.ui.search.trackAdapter.TrackAdapter
import org.guru.playlistmaker.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Collections

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModel()
    private lateinit var tracksAdapter: TrackAdapter

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private var searchQuery = SEARCH_QUERY_DEF

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) { render(it) }
        viewModel.observeShowToast().observe(viewLifecycleOwner) { showToast(it) }

        binding.apply {

            val simpleTextWatcher = object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val searchHintVisibility = searchEditTxt.hasFocus() && p0?.isEmpty() == true
                    if (searchHintVisibility) {
                        viewModel.removeSearchCallback()
                        viewModel.readTracksFromHistory()
                    }
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
                    val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    inputMethodManager?.hideSoftInputFromWindow(clearBtn.windowToken, 0)
                    viewModel.setDefaultState()
                }
            }

            searchEditTxt.clearFocus()
            searchEditTxt.apply {
                addTextChangedListener(simpleTextWatcher)
                setOnFocusChangeListener { _, hasFocus ->
                    if(hasFocus && searchEditTxt.text.isEmpty())
                        viewModel.readTracksFromHistory()
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
                    if (clickDebounce()) {
                        viewModel.addTrackToHistory(it)
                        findNavController().navigate(
                            R.id.action_searchFragment_to_playerFragment,
                            PlayerFragment.createArgs(it)
                        )
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

        if (savedInstanceState != null) {
            binding.searchEditTxt.setText(savedInstanceState.getString(SEARCH_QUERY))
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
        if (binding.searchEditTxt.text.toString().isNotEmpty())
            viewModel.searchRequest(binding.searchEditTxt.text.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeSearchCallback()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchQuery)
    }

    private fun setDefaultState() {
        tracksAdapter.tracks = Collections.emptyList()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onSearchSuccessful(list: List<Track>) {
        tracksAdapter.tracks = list
        tracksAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onSearchHistory(list: List<Track>) {
        tracksAdapter.tracks = list
        tracksAdapter.notifyDataSetChanged()
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true },
                CLICK_DEBOUNCE_DELAY
            )
        }
        return current
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
    }

    private companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
        const val SEARCH_QUERY_DEF = ""
        const val CLICK_DEBOUNCE_DELAY = 1000L

    }

}