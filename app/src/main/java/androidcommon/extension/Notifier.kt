package androidcommon.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar


fun Context.toastS(text: String): Toast {
    return Toast.makeText(this, text, Toast.LENGTH_SHORT).apply { show() }
}

fun Fragment.toastS(text: String): Toast {
    return Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).apply { show() }
}

fun Context.toastL(text: String): Toast {
    return Toast.makeText(this, text, Toast.LENGTH_LONG).apply { show() }
}

fun Fragment.toastL(text: String): Toast {
    return Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).apply { show() }
}

fun View.snackBar(@StringRes resId: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, resId, duration).show()
}

fun View.snackBar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, text, duration).show()
}

fun Fragment.snackBar(@StringRes resId: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    view?.snackBar(resId, duration)
}

fun Fragment.snackBar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT) {
    view?.snackBar(text, duration)
}

fun Activity.snackBar(@StringRes resId: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    window.decorView.findViewById<View>(android.R.id.content).snackBar(resId, duration)
}

fun Activity.snackBar(text: CharSequence, duration: Int = Snackbar.LENGTH_SHORT) {
    window.decorView.findViewById<View>(android.R.id.content).snackBar(text, duration)
}