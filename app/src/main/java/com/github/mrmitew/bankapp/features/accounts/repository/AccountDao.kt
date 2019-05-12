package com.github.mrmitew.bankapp.features.accounts.repository

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.mrmitew.bankapp.features.accounts.entity.AccountEntity
import java.math.BigDecimal

/**
 * Created by Stefan Mitev on 4-5-19.
 */

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAccounts(accounts: List<AccountEntity>)

    @Query("SELECT * FROM ${AccountEntity.TABLE_NAME} WHERE userId=:userId")
    fun getAccounts(userId: Int): LiveData<List<AccountEntity>>

    @Query("SELECT balance FROM ${AccountEntity.TABLE_NAME} WHERE id=:accountId")
    fun getAccountBalanceRefreshing(accountId: Int): LiveData<BigDecimal>

    @Query("DELETE FROM ${AccountEntity.TABLE_NAME} WHERE userId=:userId")
    suspend fun deleteAccounts(userId: Int)

    @Query("UPDATE ${AccountEntity.TABLE_NAME} SET balance=:newBalance WHERE id=:accountId")
    suspend fun updateAccountBalance(accountId: Int, newBalance: BigDecimal)
}