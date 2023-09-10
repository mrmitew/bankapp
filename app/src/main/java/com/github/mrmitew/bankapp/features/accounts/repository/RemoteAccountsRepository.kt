package com.github.mrmitew.bankapp.features.accounts.repository

import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.users.vo.User
import java.math.BigDecimal

/**
 * Repository that works with a remote data source, which exposes domain specific classes, not DTOs.
 * This way we are not going to be bound to backend specific classes so that if any changes occur
 * for any reason (ex. migration to another backend), we would only need to map the new DTOs to our
 * domain models.
 * Another benefit is that it can be mocked or faked in tests or debug builds.
 */
interface RemoteAccountsRepository {
    suspend fun fetchAccounts(user: User): List<Account>
    suspend fun fetchAccountBalance(accountId: Int): BigDecimal
    suspend fun updateAccountBalance(accountId: Int, newBalance: BigDecimal)
}
