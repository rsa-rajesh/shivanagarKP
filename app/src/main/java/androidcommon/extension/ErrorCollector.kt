package androidcommon.extension

import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.RetentionManager
import com.heartsun.shivanagarkp.App

val chuckerCollector by lazy {
    ChuckerCollector(App.instance, true, RetentionManager.Period.ONE_HOUR)
}

fun Throwable.logException(showInNotification: Boolean = true, tag: String = "EXCEPTION_TRACKER") {
    if (showInNotification) {
        chuckerCollector.onError(tag, this)
    }
    printStackTrace()
}