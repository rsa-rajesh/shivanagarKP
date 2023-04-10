package com.heartsun.shivanagarkp.di

import com.heartsun.shivanagarkp.data.database.KanipaniDatabase
import com.heartsun.shivanagarkp.data.repository.AuthRepoImpl
import com.heartsun.shivanagarkp.data.repository.AuthRepository
import com.heartsun.shivanagarkp.data.repository.ConnectionToServer
import com.heartsun.shivanagarkp.data.repository.databaseReppo.DbRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { AuthRepoImpl(get(), get(), get(), get()) }
    single { DbRepository(get()) }
    single { KanipaniDatabase.getKanipaniDatabase(androidContext(), get()) }
    single { ConnectionToServer(get()) }
}