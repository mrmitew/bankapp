package com.github.mrmitew.bankapp.features.transactions.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.transactions.vo.Transaction

/**
 * Created by Stefan Mitev on 11-5-19.
 */

class TransactionsViewModel(
    private val refreshAccountTransactionsUseCase: RefreshAccountTransactionsUseCase,
    private val getAccountBalanceUseCase: GetAccountBalanceUseCase,
    private val account: Account
) : ViewModel() {
    init {
        println("[TransactionsViewModel] ${hashCode()}")
    }

    // Create a coroutine live data (https://developer.android.com/topic/libraries/architecture/coroutines)
    // Query the business logic to get user bank accounts
    // And then map them to something that the UI can render
    val transactionListStream: LiveData<List<Transaction>> = liveData {
        emitSource(refreshAccountTransactionsUseCase(account))
    }

    val accountBalanceStream = liveData<String> {
        emitSource(getAccountBalanceUseCase(account.id).map { it.toPlainString() })
    }

    override fun onCleared() {
        super.onCleared()
        refreshAccountTransactionsUseCase.cancel()
    }
}

