package com.github.mrmitew.bankapp.features.accounts.di

import com.github.mrmitew.bankapp.features.accounts.repository.LocalAccountsRepository
import com.github.mrmitew.bankapp.features.accounts.repository.RemoteAccountsRepository
import com.github.mrmitew.bankapp.features.accounts.repository.internal.LocalAccountsRepositoryImpl
import com.github.mrmitew.bankapp.features.accounts.repository.internal.RemoteAccountsRepositoryImpl
import com.github.mrmitew.bankapp.features.accounts.ui.AccountListViewModel
import com.github.mrmitew.bankapp.features.accounts.usecase.FetchUserAccountsUseCase
import com.github.mrmitew.bankapp.features.accounts.usecase.RefreshUserAccountsUseCase
import com.github.mrmitew.bankapp.features.storage.database.AppDatabase
import com.github.mrmitew.bankapp.features.transactions.usecase.GetAccountBalanceUseCase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Stefan Mitev on 12-5-19.
 */

val accountsModule = module {
    viewModel { AccountListViewModel(get(), get()) }
    single { RefreshUserAccountsUseCase(get(), get(), get()) }
    single { FetchUserAccountsUseCase(get(), get(), get()) }
    single { RemoteAccountsRepositoryImpl(get(), get()) as RemoteAccountsRepository }
    single { LocalAccountsRepositoryImpl(get()) as LocalAccountsRepository }
    single { get<AppDatabase>().accountDao() }
    single { GetAccountBalanceUseCase(get(), get()) }
}
