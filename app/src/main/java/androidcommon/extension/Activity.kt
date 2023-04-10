package androidcommon.extension

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.heartsun.shivanagarkp.R

fun AppCompatActivity.setBackNavigation(
    toolbar: Toolbar,
    title: String? = "Tiffin Batta",
    @DrawableRes navIcon: Int = R.drawable.ic_baseline_arrow
) {
    setSupportActionBar(toolbar)
    toolbar.setNavigationIcon(navIcon)
    toolbar.setNavigationOnClickListener { onBackPressed() }
    supportActionBar?.title = title
    //toolbar.title = title
}

fun Fragment.setNavigationIcon(
    toolbar: Toolbar,
    title: String? = "Tiffin Batta",
    @DrawableRes navIcon: Int = R.drawable.ic_baseline_arrow
) {
    (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
    toolbar.setNavigationIcon(navIcon)
    toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
    (activity as AppCompatActivity).supportActionBar?.title = title
}

fun AppCompatActivity.setBackNavigationTwo(
    toolbar: Toolbar,
    @DrawableRes navIcon: Int = R.drawable.ic_baseline_arrow
) {
    setSupportActionBar(toolbar)
    toolbar.setNavigationIcon(navIcon)
    toolbar.setNavigationOnClickListener { onBackPressed() }
}

fun Activity.showKeyboard() {
    val imm: InputMethodManager =
        getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}