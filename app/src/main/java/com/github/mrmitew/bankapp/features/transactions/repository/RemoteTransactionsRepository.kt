package com.github.mrmitew.bankapp.features.transactions.repository

import com.github.mrmitew.bankapp.features.transactions.vo.Transaction

interface RemoteTransactionsRepository {
    suspend fun getTransactions(accountId: Int): List<Transaction>
    suspend fun addTransaction(transaction: Transaction)
}