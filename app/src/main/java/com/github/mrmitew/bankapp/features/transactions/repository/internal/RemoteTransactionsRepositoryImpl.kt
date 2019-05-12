package com.github.mrmitew.bankapp.features.transactions.repository.internal

import com.github.mrmitew.bankapp.APP_TOKEN
import com.github.mrmitew.bankapp.features.transactions.dto.toDTO
import com.github.mrmitew.bankapp.features.transactions.dto.toDomainModel
import com.github.mrmitew.bankapp.features.transactions.repository.RemoteTransactionsRepository
import com.github.mrmitew.bankapp.features.transactions.vo.Transaction
import com.github.mrmitew.bankapp.features.users.repository.AuthService
import com.github.mrmitew.bankapp.features.users.repository.BackendApi

class RemoteTransactionsRepositoryImpl(
    private val backend: BackendApi,
    private val authService: AuthService
) :
    RemoteTransactionsRepository {

    override suspend fun getTransactions(accountId: Int): List<Transaction> =
        backend.fetchTransactions(authService.getUserToken(APP_TOKEN), accountId)
            .map { it.toDomainModel() }


    override suspend fun addTransaction(transaction: Transaction) {
        backend.addTransaction(authService.getUserToken(APP_TOKEN), transaction.toDTO())
    }
}