package androidcommon.extension

import android.view.View
import android.view.animation.AlphaAnimation

fun View.fadeInAnimation(duration: Long = 1200) {
    val fadeOut = AlphaAnimation(1.0f, 0.0f)
    val fadeIn = AlphaAnimation(0.0f, 1.0f)
    fadeOut.duration = duration
    fadeOut.fillAfter = true
    fadeIn.duration = duration
    fadeIn.fillAfter = true
    fadeIn.startOffset = 100 + fadeOut.startOffset
    this.startAnimation(fadeIn)
}