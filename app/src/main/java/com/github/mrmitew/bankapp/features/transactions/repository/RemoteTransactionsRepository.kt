package com.github.mrmitew.bankapp.features.transactions.repository

import com.github.mrmitew.bankapp.features.transactions.vo.Transaction

/**
 * Repository that works with a remote data source, which exposes domain specific classes, not DTOs.
 * This way we are not going to be bound to backend specific classes so that if any changes occur
 * for any reason (ex. migration to another backend), we would only need to map the new DTOs to our
 * domain models.
 * Another benefit is that it can be mocked or faked in tests or debug builds.
 */
interface RemoteTransactionsRepository {
    suspend fun getTransactions(accountId: Int): List<Transaction>
    suspend fun addTransaction(transaction: Transaction)
}
