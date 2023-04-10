package com.heartsun.shivanagarkp.customView

import android.R
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.StringRes


/**
 * Drawable that generates the two pieces of text in the track of the switch, one of each
 * side of the positions of the thumb.
 */
class SwitchTrackTextDrawable(
    context: Context,
    @StringRes leftTextId: Int,
    @StringRes rightTextId: Int
) : Drawable() {
    private val mContext: Context = context
    private val mLeftText: String = context.getString(leftTextId)
    private val mRightText: String
    private val mTextPaint: Paint
    private fun createTextPaint(): Paint {
        val textPaint = Paint()
        textPaint.color = mContext.resources.getColor(R.color.white)
        textPaint.isAntiAlias = true
        textPaint.style = Paint.Style.FILL
        textPaint.textAlign = Paint.Align.CENTER
        // Set textSize, typeface, etc, as you wish
        return textPaint
    }

    override fun draw(canvas: Canvas) {
        val textBounds = Rect()
        mTextPaint.getTextBounds(mRightText, 0, mRightText.length, textBounds)

        // The baseline for the text: centered, including the height of the text itself
        val heightBaseline: Int = canvas.clipBounds.height() / 2 + textBounds.height() / 2

        // This is one quarter of the full width, to measure the centers of the texts
        val widthQuarter: Int = canvas.clipBounds.width() / 4
        canvas.drawText(
            mLeftText, 0, mLeftText.length,
            widthQuarter.toFloat(), heightBaseline.toFloat(),
            mTextPaint
        )
        canvas.drawText(
            mRightText, 0, mRightText.length,
            (widthQuarter * 3).toFloat(), heightBaseline.toFloat(),
            mTextPaint
        )
    }

    override fun setAlpha(alpha: Int) {}
    override fun setColorFilter(cf: ColorFilter?) {}
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    init {

        // Left text
        mTextPaint = createTextPaint()

        // Right text
        mRightText = context.getString(rightTextId)
    }
}