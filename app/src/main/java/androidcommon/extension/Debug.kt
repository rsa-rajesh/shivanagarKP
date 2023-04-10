package androidcommon.extension

import com.heartsun.shivanagarkp.BuildConfig


const val developerTest: Boolean = true
inline fun debugOnly(func: () -> Unit) {
    if (BuildConfig.DEBUG) {
        func.invoke()
    }
}

inline fun developerTestOnly(func: () -> Unit) {
    if (developerTest && BuildConfig.DEBUG) {
        func.invoke()
    }
}