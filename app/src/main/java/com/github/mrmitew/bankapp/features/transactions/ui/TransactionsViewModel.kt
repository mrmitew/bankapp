package com.github.mrmitew.bankapp.features.transactions.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.PagingData
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.transactions.usecase.GetAccountBalanceUseCase
import com.github.mrmitew.bankapp.features.transactions.usecase.RefreshAccountTransactionsUseCase
import com.github.mrmitew.bankapp.features.transactions.vo.Transaction

/**
 * Created by Stefan Mitev on 11-5-19.
 */

class TransactionsViewModel(
    private val refreshAccountTransactionsUseCase: RefreshAccountTransactionsUseCase,
    private val getAccountBalanceUseCase: GetAccountBalanceUseCase,
    private val account: Account
) : ViewModel() {
    // Create a coroutine live data (https://developer.android.com/topic/libraries/architecture/coroutines)
    // Query the business logic to get user bank accounts
    // And then map them to something that the UI can render
    val transactionListStream: LiveData<PagingData<Transaction>> = liveData {
        emitSource(refreshAccountTransactionsUseCase(account))
    }

    val accountBalanceStream = liveData<String> {
        emitSource(getAccountBalanceUseCase(account.id).map { it.toPlainString() })
    }

    override fun onCleared() {
        super.onCleared()
        // Since we are using an internal coroutine scope in this use case,
        // we have to make sure we cancel the pending jobs (if any)
        refreshAccountTransactionsUseCase.cancel()
    }
}

