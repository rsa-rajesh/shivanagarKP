package androidcommon.extension

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


fun Context.compatColor(@ColorRes id: Int) = ContextCompat.getColor(this, id)

fun Fragment.compatColor(color: Int): Int = ContextCompat.getColor(requireContext(), color)

fun Context.compatDrawable(drawable: Int): Drawable? = ContextCompat.getDrawable(this, drawable)

fun Fragment.compatDrawable(drawable: Int): Drawable? =
    ContextCompat.getDrawable(requireContext(), drawable)