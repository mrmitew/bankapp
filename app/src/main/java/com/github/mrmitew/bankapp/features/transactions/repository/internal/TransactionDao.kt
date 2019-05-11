package com.github.mrmitew.bankapp.features.transactions.repository.internal

import androidx.lifecycle.LiveData
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

    @Query("SELECT * FROM ${TransactionEntity.TABLE_NAME} WHERE accountId=:accountId")
    fun getTransactions(accountId: Int): LiveData<List<TransactionEntity>>

    @Query("DELETE FROM ${TransactionEntity.TABLE_NAME} WHERE accountId=:accountId")
    suspend fun deleteTransactions(accountId: Int)
}