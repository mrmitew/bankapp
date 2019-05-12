package com.github.mrmitew.bankapp.features.accounts.repository

import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.users.vo.User
import java.math.BigDecimal

/**
 * Repository that works with remote data source
 */
interface RemoteAccountsRepository {
    suspend fun fetchAccounts(user: User): List<Account>
    suspend fun fetchAccountBalance(accountId: Int): BigDecimal
    suspend fun updateAccountBalance(accountId: Int, newBalance: BigDecimal)
}