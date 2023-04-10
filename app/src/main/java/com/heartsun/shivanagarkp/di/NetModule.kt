package com.heartsun.shivanagarkp.di

import android.content.Context
import androidcommon.extension.developerTestOnly
import com.heartsun.shivanagarkp.data.Prefs
import com.heartsun.shivanagarkp.di.DataSourceProperties.APP_NAME
import com.heartsun.shivanagarkp.di.NetConfig.CONNECT_TIMEOUT
import com.heartsun.shivanagarkp.di.NetConfig.READ_TIMEOUT
import com.heartsun.shivanagarkp.di.NetConfig.WRITE_TIMEOUT
import com.heartsun.shivanagarkp.utils.UserAgentInterceptor
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.heartsun.shivanagarkp.BuildConfig
import com.heartsun.shivanagarkp.data.apis.AuthApi
import com.heartsun.shivanagarkp.di.DataSourceProperties.SERVER_URL
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

val netModule = module {
    single { provideGson() }
    single { provideChuckerInterceptor(get()) }
    single { provideLoggingInterceptor() }
    single { provideHeaderInterceptor(get()) }
    factory { provideRestApiService<AuthApi>(SERVER_URL, get(), get(), get(), get(), get()) }
}

object DataSourceProperties {

    const val APP_NAME = "Khana Pani"

    const val SERVER_URL = BuildConfig.BASE_URL
    const val BASE_PATH = "dropcare"

    const val PRIVACY_POLICY = BuildConfig.BASE_URL + "api/v1/constants/privacy-policy/"
    const val TERMS_AND_CONDITIONS = BuildConfig.BASE_URL + "api/v1/constants/tac/"
    const val ABOUT = BuildConfig.BASE_URL + "api/v1/constants/about-us/"
    const val FAQ = BuildConfig.BASE_URL + "api/v1/constants/about-us/"
}

object NetConfig {
    const val CONNECT_TIMEOUT = 10L
    const val READ_TIMEOUT = 20L
    const val WRITE_TIMEOUT = 60L
}

private fun provideGson(): Gson = GsonBuilder().setPrettyPrinting().create()
private fun provideChuckerInterceptor(context: Context): ChuckerInterceptor {
    return ChuckerInterceptor.Builder(context).build()
}

private fun provideLoggingInterceptor() =
    HttpLoggingInterceptor { message ->
        //logger(message, "logRetrofit")
    }.apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

private fun provideHeaderInterceptor(prefs: Prefs): Interceptor =
    Interceptor { chain ->
        val version = BuildConfig.VERSION_NAME
        val versionCode = BuildConfig.VERSION_CODE

        val request = chain.request().newBuilder()
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("App-Name", APP_NAME)
            .header("App-Version", version)
            .header("Build-Number", versionCode.toString())
            .header("Platform", "Android")
            // .header("Unique-Id", prefs.uniqueId)
            .build()

        chain.proceed(request)
    }

inline fun <reified T> provideRestApiService(
    serverUrl: String,
    context: Context,
    prefs: Prefs,
    headerInterceptor: Interceptor,
    loggingInterceptor: HttpLoggingInterceptor,
    chuckerInterceptor: ChuckerInterceptor,
    addInterceptors: Boolean = true
): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(serverUrl)
        .client(
            createRestOkHttpClient(
                context,
                loggingInterceptor,
                headerInterceptor,
                chuckerInterceptor,
                addInterceptors,
                prefs
            )
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    return retrofit.create()
}

fun createRestOkHttpClient(
    context: Context,
    loggingInterceptor: HttpLoggingInterceptor,
    headerInterceptor: Interceptor,
    chuckerInterceptor: ChuckerInterceptor,
    addInterceptors: Boolean = false,
    prefs: Prefs
): OkHttpClient {
    val builder = OkHttpClient().newBuilder()
        .cache(Cache(context.cacheDir, 10 * 1024 * 1024))  // 10MB
        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.MINUTES)
        .readTimeout(READ_TIMEOUT, TimeUnit.MINUTES)
        .writeTimeout(WRITE_TIMEOUT, TimeUnit.MINUTES)
        .addInterceptor(headerInterceptor)
        .addInterceptor(UserAgentInterceptor())
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
            prefs.authToken?.let { token ->
                requestBuilder.header("Authorization", token)
            }
            chain.proceed(requestBuilder.build())
        }

    developerTestOnly {
        if (addInterceptors) {
            builder
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chuckerInterceptor)
        }
    }

    return builder.build()
}




