package com.github.mrmitew.bankapp.features.login.di

import com.github.mrmitew.bankapp.features.login.ui.LoginViewModel
import com.github.mrmitew.bankapp.features.users.usecase.LogInUserUseCase
import com.github.mrmitew.bankapp.features.users.usecase.LogOutUserUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Stefan Mitev on 12-5-19.
 */

val loginModule = module {
    viewModel { LoginViewModel(get()) }
    single { LogInUserUseCase(get(), get(), get()) }
    single { LogOutUserUseCase(get()) }
}