package androidcommon.utils

import android.text.format.DateUtils
import com.heartsun.shivanagarkp.StandardDateTimeFormat
import java.text.SimpleDateFormat
import java.util.*

fun String.convertToDate(providedFormat: String): Date? {
    val sdf = SimpleDateFormat(providedFormat, Locale.getDefault())
    return sdf.parse(this)
}

fun String.stringToDate(
    requiredFormat: String = "MMM dd, yyyy",
    dateTimeFormat: String = StandardDateTimeFormat
): String {
    return try {
        val date: Date? = this.convertToDate(dateTimeFormat)
        val sdf2 = SimpleDateFormat(requiredFormat, Locale.getDefault())
        sdf2.format(date!!)
    } catch (ex: Exception) {
        "N/A"
    }
}

fun String.convertToRelativeTimeSpam(providedFormat: String): String {
    return convertToDate(providedFormat)?.let {
        DateUtils.getRelativeTimeSpanString(
            it.time,
            Calendar.getInstance().timeInMillis,
            DateUtils.MINUTE_IN_MILLIS
        )
    }?.toString().orEmpty()
}

