package com.github.mrmitew.bankapp.features.transactions.repository

import androidx.lifecycle.LiveData
import com.github.mrmitew.bankapp.features.transactions.vo.Transaction

interface TransactionRepository {
    fun getTransactions(accountId: Int): LiveData<List<Transaction>>
    suspend fun addTransaction(transaction: Transaction)
}