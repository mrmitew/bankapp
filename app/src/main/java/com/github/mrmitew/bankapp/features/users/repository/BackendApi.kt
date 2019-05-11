package com.github.mrmitew.bankapp.features.users.repository

import com.github.mrmitew.bankapp.features.transactions.dto.TransactionDTO
import com.github.mrmitew.bankapp.features.users.dto.UserDTO

/**
 * Since we are not going to communicate with an actual backend, we'll pretend as if there is one.
 * That is the public api /endpoints/ of our fake backend.
 * Notice that we expose DTOs here like [UserDTO].
 */
interface BackendApi {
    fun getUserToken(username: String): String
    fun getPerson(token: String): UserDTO
    fun getTransactions(userAccessToken: String, accountId: Int): List<TransactionDTO>
    suspend fun addTransaction(userAccessToken: String, transaction: TransactionDTO)
}