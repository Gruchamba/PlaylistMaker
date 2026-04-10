package org.guru.playlistmaker.ui.util

import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.TypedValue
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