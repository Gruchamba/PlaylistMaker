package org.guru.playlistmaker

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    lateinit var clearBtn: ImageView
    lateinit var searchEditTxt: EditText
    private var searchQuery = SEARCH_QUERY_DEF

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                searchQuery = p0.toString()
                clearBtn.visibility = if (searchQuery.isEmpty()) View.GONE else View.VISIBLE
            }

        }

        clearBtn = findViewById(R.id.cleatBtn)
        clearBtn.visibility = View.GONE
        clearBtn.setOnClickListener {
            searchEditTxt.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(clearBtn.windowToken, 0)
        }

        searchEditTxt = findViewById(R.id.searchEditTxt)
        searchEditTxt.addTextChangedListener(simpleTextWatcher)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY, searchQuery)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchEditTxt.setText(savedInstanceState.getString(SEARCH_QUERY))
    }

    companion object {
        const val SEARCH_QUERY = "SEARCH_QUERY"
        const val SEARCH_QUERY_DEF = ""
    }
}