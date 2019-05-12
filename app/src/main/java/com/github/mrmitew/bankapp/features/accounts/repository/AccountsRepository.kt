package com.github.mrmitew.bankapp.features.accounts.repository

import androidx.lifecycle.LiveData
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.users.vo.User
import java.math.BigDecimal

/**
 * Created by Stefan Mitev on 4-5-19.
 */

interface AccountsRepository {
    suspend fun getAccounts(user: User): LiveData<List<Account>>
    suspend fun storeAccounts(user: User, accounts: List<Account>)
    suspend fun deleteAccounts(user: User)
}

interface LocalAccountsRepository : AccountsRepository {
    suspend fun getAccountBalanceRefreshing(accountId: Int): LiveData<BigDecimal>
    suspend fun updateAccountBalance(accountId: Int, newBalance: BigDecimal)
}

interface RemoteAccountsRepository : AccountsRepository {
    suspend fun fetchAccountBalance(accountId: Int): BigDecimal
    suspend fun updateAccountBalance(accountId: Int, newBalance: BigDecimal)
}