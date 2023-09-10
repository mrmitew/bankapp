package com.github.mrmitew.bankapp.features.transactions.di

import com.github.mrmitew.bankapp.R
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.storage.database.AppDatabase
import com.github.mrmitew.bankapp.features.transactions.repository.LocalTransactionsRepository
import com.github.mrmitew.bankapp.features.transactions.repository.RemoteTransactionsRepository
import com.github.mrmitew.bankapp.features.transactions.repository.internal.LocalTransactionsRepositoryImpl
import com.github.mrmitew.bankapp.features.transactions.repository.internal.RemoteTransactionsRepositoryImpl
import com.github.mrmitew.bankapp.features.transactions.ui.AddTransactionUseCase
import com.github.mrmitew.bankapp.features.transactions.ui.AddTransactionViewModel
import com.github.mrmitew.bankapp.features.transactions.ui.GetAvailableAccountsForTransactionUseCase
import com.github.mrmitew.bankapp.features.transactions.ui.TransactionsViewModel
import com.github.mrmitew.bankapp.features.transactions.usecase.RefreshAccountTransactionsUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Created by Stefan Mitev on 12-5-19.
 */

val transactionsModule = module {
    viewModel { (account: Account) -> TransactionsViewModel(get(), get(), account) } // Assisted injection
    viewModel { (account: Account) -> AddTransactionViewModel(account, get(), get()) } // Assisted injection
    single { LocalTransactionsRepositoryImpl(get()) as LocalTransactionsRepository }
    single { RemoteTransactionsRepositoryImpl(get(), get()) as RemoteTransactionsRepository }
    single { get<AppDatabase>().transactionDao() }
    single {
        AddTransactionUseCase(
            androidContext().getString(R.string.deposit),
            androidContext().getString(R.string.withdraw),
            get(),
            get(),
            get(),
            get()
        )
    }
    factory { RefreshAccountTransactionsUseCase(get(), get()) }
    factory { GetAvailableAccountsForTransactionUseCase(get(), get()) }
}
