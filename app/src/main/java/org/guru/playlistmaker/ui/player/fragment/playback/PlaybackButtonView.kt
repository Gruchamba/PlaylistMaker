package org.guru.playlistmaker.ui.player.fragment.playback

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import org.guru.playlistmaker.R

internal class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val playImageBitmap: Bitmap?
    private val pauseImageBitmap: Bitmap?
    private var imageRect = RectF(0f, 0f, 0f, 0f)

    enum class State { PLAY, PAUSE }
    private var currentState: State = State.PAUSE

    init {

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackImage,
            defStyleAttr,
            defStyleRes
        ).apply {

            isClickable = true

            try {

                playImageBitmap = getDrawable(R.styleable.PlaybackImage_playImageResId)?.toBitmap()
                pauseImageBitmap = getDrawable(R.styleable.PlaybackImage_pauseImageResId)?.toBitmap()

            } finally {
                recycle()
            }
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                changeStateView()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        when(currentState) {
            State.PLAY -> {
                playImageBitmap?.let {
                    canvas.drawBitmap(it, null, imageRect, null)
                }
            }
            State.PAUSE -> {
                pauseImageBitmap?.let {
                    canvas.drawBitmap(it, null, imageRect, null)
                }
            }
        }
    }

    fun changeStateView() {
        currentState = if (currentState == State.PLAY) State.PAUSE else State.PLAY
        invalidate()
    }

    fun setState(state: State) {
        currentState = state
        invalidate()
    }
}