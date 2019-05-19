package com.github.mrmitew.bankapp.features.transactions.repository.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.github.mrmitew.bankapp.features.transactions.entity.toDomainModel
import com.github.mrmitew.bankapp.features.transactions.entity.toEntity
import com.github.mrmitew.bankapp.features.transactions.repository.LocalTransactionsRepository
import com.github.mrmitew.bankapp.features.transactions.vo.Transaction

/**
 * Implementation of a repository that uses a local data source to work with
 * account transactions.
 */
class LocalTransactionsRepositoryImpl(private val transactionDao: TransactionDao) :
    LocalTransactionsRepository {

    override fun getTransactions(accountId: Int): LiveData<PagedList<Transaction>> {
        return transactionDao.getTransactions(accountId)
            .map { it.toDomainModel() }
            .toLiveData(
                config = Config(
                    pageSize = 50,
                    prefetchDistance = 150,
                    enablePlaceholders = true
                )
            ).distinctUntilChanged()
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