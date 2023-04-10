package com.heartsun.shivanagarkp.di


import com.heartsun.shivanagarkp.ui.HomeViewModel
import com.heartsun.shivanagarkp.ui.memberRegisterRequest.RegisterViewModel
import com.heartsun.shivanagarkp.ui.meroKhaniPani.MyTapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get(),get()) }
    viewModel { RegisterViewModel(get(),get()) }
    viewModel { MyTapViewModel(get(),get()) }

}


