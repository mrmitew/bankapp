package com.github.mrmitew.bankapp.features.transactions.repository.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import com.github.mrmitew.bankapp.features.transactions.entity.toDomainModel
import com.github.mrmitew.bankapp.features.transactions.entity.toEntity
import com.github.mrmitew.bankapp.features.transactions.repository.LocalTransactionsRepository
import com.github.mrmitew.bankapp.features.transactions.vo.Transaction

class RoomTransactionsRepository(private val transactionDao: TransactionDao) :
    LocalTransactionsRepository {

    override fun getTransactions(accountId: Int): LiveData<List<Transaction>> {
        return transactionDao.getTransactions(accountId).distinctUntilChanged()
            .map { transactionsEntity -> transactionsEntity.map { it.toDomainModel() } }
    }

    override suspend fun deleteTransactions(accountId: Int) {
        transactionDao.deleteTransactions(accountId)
    }

    override suspend fun addTransactions(transactions: List<Transaction>) {
        transactionDao.addTransactions(transactions.map { it.toEntity() })
    }

    override suspend fun addTransaction(transaction: Transaction) {
        transactionDao.addTransaction(transaction.toEntity())
    }
}