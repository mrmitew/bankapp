package com.github.mrmitew.bankapp.features.users.repository

import com.github.mrmitew.bankapp.features.accounts.dto.AccountDTO
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.transactions.dto.TransactionDTO
import com.github.mrmitew.bankapp.features.users.dto.UserDTO
import com.github.mrmitew.bankapp.features.users.vo.User
import java.math.BigDecimal

/**
 * Since we are not going to communicate with an actual backend, we'll pretend as if there is one.
 * That is the public api /endpoints/ of our fake backend.
 * Notice that we expose DTOs here like [UserDTO].
 */
interface BackendApi {
    suspend fun fetchUserToken(username: String): String
    suspend fun fetchPerson(token: String): UserDTO
    suspend fun fetchTransactions(userAccessToken: String, accountId: Int): List<TransactionDTO>
    suspend fun addTransaction(userAccessToken: String, transaction: TransactionDTO)
    suspend fun fetchAccounts(user: User): List<AccountDTO>
    suspend fun fetchAccountBalance(accountId: Int): BigDecimal
    suspend fun updateAccountBalance(accountId: Int, newBalance: BigDecimal)
}