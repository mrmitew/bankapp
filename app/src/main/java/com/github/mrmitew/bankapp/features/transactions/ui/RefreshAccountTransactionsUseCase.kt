package com.github.mrmitew.bankapp.features.transactions.ui

import androidx.lifecycle.LiveData
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.common.usecase.UseCase
import com.github.mrmitew.bankapp.features.common.vo.catchResult
import com.github.mrmitew.bankapp.features.common.vo.onFailure
import com.github.mrmitew.bankapp.features.transactions.repository.LocalTransactionsRepository
import com.github.mrmitew.bankapp.features.transactions.repository.RemoteTransactionsRepository
import com.github.mrmitew.bankapp.features.transactions.vo.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class RefreshAccountTransactionsUseCase(
    private val localTransactionsRepository: LocalTransactionsRepository,
    private val remoteTransactionsRepository: RemoteTransactionsRepository
) :
    UseCase<Account, LiveData<List<Transaction>>>, CoroutineScope by MainScope() {
    override suspend fun invoke(param: Account): LiveData<List<Transaction>> {
        launch {
            catchResult {
                val transactions = remoteTransactionsRepository.getTransactions(param.id)
                if (transactions.isNotEmpty()) {
                    localTransactionsRepository.addTransactions(transactions)
                } else {
                    localTransactionsRepository.deleteTransactions(param.id)
                }
            }.onFailure {
                // Log
                it.printStackTrace()
            }
        }
        return localTransactionsRepository.getTransactions(accountId = param.id)
    }
}