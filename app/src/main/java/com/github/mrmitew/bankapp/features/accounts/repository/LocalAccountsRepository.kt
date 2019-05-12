package com.github.mrmitew.bankapp.features.accounts.repository

import androidx.lifecycle.LiveData
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.users.vo.User
import java.math.BigDecimal

/**
 * Created by Stefan Mitev on 4-5-19.
 */

/**
 * Repository that works with local data source.
 * It can be mocked or faked in tests or debug builds.
 */
interface LocalAccountsRepository {
    suspend fun getAccounts(user: User): List<Account>
    suspend fun getAccountsRefreshing(user: User): LiveData<List<Account>>
    suspend fun getAccountBalanceRefreshing(accountId: Int): LiveData<BigDecimal>
    suspend fun storeAccounts(user: User, accounts: List<Account>)
    suspend fun updateAccountBalance(accountId: Int, newBalance: BigDecimal)
    suspend fun deleteAccounts(user: User)
}