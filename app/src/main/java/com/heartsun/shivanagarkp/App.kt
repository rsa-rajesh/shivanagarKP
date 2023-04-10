package com.heartsun.shivanagarkp

import android.app.Application
import androidcommon.extension.plantLogger
import com.heartsun.shivanagarkp.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    companion object {
        lateinit var instance: App
    }
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule, viewModelModule, storageModule, netModule, repositoryModule)
            androidLogger()
        }
        instance = this
        plantLogger()
    }
}