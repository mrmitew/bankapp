package com.github.mrmitew.bankapp.features.common.di

import com.github.mrmitew.bankapp.features.transactions.usecase.GetAccountBalanceUseCase
import org.koin.dsl.module

/**
 * Created by Stefan Mitev on 12-5-19.
 */

val commonModule = module {
    single { GetAccountBalanceUseCase(get(), get()) }
}