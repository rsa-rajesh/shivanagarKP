package com.heartsun.shivanagarkp.utils

import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response

class UserAgentInterceptor : Interceptor {

    companion object {
        /**
         * DO NOT MODIFY THIS FUNCTION!! ANY MODIFICATION MAY CAUSE UNINTENDED BUGS
         * */
        fun getDefaultUserAgent(): String {
            val result = StringBuilder(64)
            result.append("Dalvik/")
            result.append(System.getProperty("java.vm.version")) // such as 1.1.0
            result.append(" (Linux; U; Android ")
            val version = Build.VERSION.RELEASE // "1.0" or "3.4b5"
            result.append(if (version.isNotEmpty()) version else "1.0")

            // add the model for the release build
            if ("REL" == Build.VERSION.CODENAME) {
                val model = Build.MODEL
                if (model.isNotEmpty()) {
                    result.append("; ")
                    result.append(model)
                }
            }
            val id = Build.ID // "MASTER" or "M4-rc20"
            if (id.isNotEmpty()) {
                result.append(" Build/")
                result.append(id)
            }
            result.append(")")
            return result.toString()
        }
    }

    private val userAgent: String by lazy {
        getDefaultUserAgent()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.header("User-Agent", userAgent)
        return chain.proceed(builder.build())
    }
}