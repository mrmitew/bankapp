package com.github.mrmitew.bankapp.features.transactions.repository.internal

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.mrmitew.bankapp.features.transactions.entity.TransactionEntity

/**
 * Created by Stefan Mitev on 11-5-19.
 */

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransactions(transactions: List<TransactionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTransaction(transaction: TransactionEntity)

    // Since dates are not formatted properly, ordering doesn't really work as it should.
    // We could use an sql function to parse the date, but didn't want to spend time on this.
    @Query("SELECT * FROM ${TransactionEntity.TABLE_NAME} WHERE accountId=:accountId ORDER BY date DESC")
    fun getTransactions(accountId: Int): DataSource.Factory<Int, TransactionEntity>

    @Query("DELETE FROM ${TransactionEntity.TABLE_NAME} WHERE accountId=:accountId")
    suspend fun deleteTransactions(accountId: Int)
}