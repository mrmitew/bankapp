package com.github.mrmitew.bankapp.features.transactions.repository.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.github.mrmitew.bankapp.APP_TOKEN
import com.github.mrmitew.bankapp.features.transactions.repository.RemoteTransactionsRepository
import com.github.mrmitew.bankapp.features.transactions.vo.Transaction
import com.github.mrmitew.bankapp.features.transactions.vo.toDTO
import com.github.mrmitew.bankapp.features.transactions.vo.toDomainModel
import com.github.mrmitew.bankapp.features.users.repository.AuthService
import com.github.mrmitew.bankapp.features.users.repository.BackendApi

class FakeRemoteTransactionsRepository(
    private val backend: BackendApi,
    private val authService: AuthService
) :
    RemoteTransactionsRepository {

    override fun getTransactions(accountId: Int): LiveData<List<Transaction>> =
        liveData {
            backend.getTransactions(authService.getUserToken(APP_TOKEN), accountId)
                .map { it.toDomainModel() }
        }


    override suspend fun addTransaction(transaction: Transaction) {
        backend.addTransaction(authService.getUserToken(APP_TOKEN), transaction.toDTO())
    }
}