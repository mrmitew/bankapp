package com.github.mrmitew.bankapp.features.transactions.repository

import com.github.mrmitew.bankapp.features.transactions.vo.Transaction

interface LocalTransactionsRepository : TransactionRepository {
    suspend fun deleteTransactions(accountId: Int)
    suspend fun addTransactions(transactions: List<Transaction>)
}