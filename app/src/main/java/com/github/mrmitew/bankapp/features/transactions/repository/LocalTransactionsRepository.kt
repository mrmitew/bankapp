package com.github.mrmitew.bankapp.features.transactions.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.github.mrmitew.bankapp.features.transactions.vo.Transaction

/**
 * Repository that works with local data source.
 * It can be mocked or faked in tests or debug builds.
 */
interface LocalTransactionsRepository  {
    fun getTransactions(accountId: Int): LiveData<PagingData<Transaction>>
    suspend fun addTransaction(transaction: Transaction)
    suspend fun deleteTransactions(accountId: Int)
    suspend fun addTransactions(transactions: List<Transaction>)
}