package androidcommon.extension

import com.google.gson.Gson
import timber.log.Timber


fun logger(msg: String?) {
    debugOnly { Timber.d(msg) }
}

fun loggerE(msg: String?) {
    Timber.e(msg)/* debugOnly { }*/
}

fun logger(msg: String?, tag: String) {
    debugOnly { Timber.tag(tag).d(msg) }
}

fun loggerE(msg: String?, tag: String) {
    debugOnly { Timber.tag(tag).e(msg) }
}
fun loggerI(msg: String?) {
    Timber.i(msg)/* debugOnly { }*/
}
fun loggerI(msg: String?, tag: String) {
    debugOnly { Timber.tag(tag).i(msg) }
}
fun loggerJ(any: Any?) {
    debugOnly { Timber.d(any.toJson()) }
}

fun Any?.toJson(): String? {
    return Gson().newBuilder()
        .setPrettyPrinting()
        .create()
        .toJson(this ?: return null)
}

fun plantLogger() {
    debugOnly { Timber.plant(Timber.DebugTree()) }
}
