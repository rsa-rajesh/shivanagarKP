package com.heartsun.shivanagarkp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.heartsun.shivanagarkp.data.Prefs
import com.heartsun.shivanagarkp.di.PersistenceDataSourceProperties.PREF_NAME
import org.koin.dsl.module

val storageModule = module {
    single { providePrefsManager(get()) }
    single { PREF_NAME.provideSharedPreference(get()) }
}

object PersistenceDataSourceProperties {
    const val PREF_NAME = "user_prefs"
}

private fun String.provideSharedPreference(context: Context): SharedPreferences {
    val masterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    return EncryptedSharedPreferences.create(
        context, this, masterKey, EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}

private fun providePrefsManager(sharedPreferences: SharedPreferences): Prefs {
    return Prefs(sharedPreferences)
}
