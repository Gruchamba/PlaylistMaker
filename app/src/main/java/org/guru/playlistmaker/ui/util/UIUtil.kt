package org.guru.playlistmaker.ui.util

import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar
import org.guru.playlistmaker.R
import org.guru.playlistmaker.ui.library.newPlaylist.fragment.CreateOrUpdatePlaylistFragment.Companion.LOCAL_STORAGE_FOR_IMAGE
import java.io.File

fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics).toInt()
}

fun loadImageFromLocalStorage(context: Context, fileName: String?) : Uri? {
    if (!fileName.isNullOrEmpty()) {
        val file = File(
            context.getDir(
                LOCAL_STORAGE_FOR_IMAGE,
                Context.MODE_PRIVATE),
            fileName
        )

        return if (file.exists()) Uri.fromFile(file) else null
    } else { return null }
}

fun showCustomSnackbar(layoutInflater: LayoutInflater, viewGroup: ViewGroup, message: String) {

    val customView = layoutInflater.inflate(R.layout.playlist_maker_snackbar, null)
    val textView = customView.findViewById<TextView>(R.id.snackbar_message)
    textView.text = message

    val snackbar = Snackbar.make(viewGroup, "", Snackbar.LENGTH_LONG)

    val snackbarView = snackbar.view
    val params = snackbarView.layoutParams as CoordinatorLayout.LayoutParams

    val marginPx = viewGroup.resources.getDimensionPixelSize(R.dimen.snackbar_margin)
    params.setMargins(marginPx, 0, marginPx, marginPx)
    snackbarView.layoutParams = params

    (snackbarView as? ViewGroup)?.addView(customView, 0)

    snackbar.show()
}