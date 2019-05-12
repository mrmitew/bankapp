package com.github.mrmitew.bankapp.features.transactions.repository

import com.github.mrmitew.bankapp.features.transactions.vo.Transaction

/**
 * Repository that works with remote data source.
 * It can be mocked or faked in tests or debug builds.
 */
interface RemoteTransactionsRepository {
    suspend fun getTransactions(accountId: Int): List<Transaction>
    suspend fun addTransaction(transaction: Transaction)
}