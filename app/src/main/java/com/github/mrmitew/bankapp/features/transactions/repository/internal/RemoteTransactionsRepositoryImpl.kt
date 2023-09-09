package com.github.mrmitew.bankapp.features.transactions.repository.internal

import com.github.mrmitew.bankapp.features.auth.AuthService
import com.github.mrmitew.bankapp.features.auth.di.APP_TOKEN
import com.github.mrmitew.bankapp.features.backend.BackendApi
import com.github.mrmitew.bankapp.features.transactions.dto.toDTO
import com.github.mrmitew.bankapp.features.transactions.dto.toDomainModel
import com.github.mrmitew.bankapp.features.transactions.repository.RemoteTransactionsRepository
import com.github.mrmitew.bankapp.features.transactions.vo.Transaction

/**
 * Implementation of a repository that uses a remote data source to work with
 * account transactions.
 *
 * [backendApi] can be a Retrofit instance here.. but it isn't.
 * We faked it.. Though its just an interface.. only the dependency
 * injection framework knows the actual implementation
 *
 * Every call requires a user token, so we'll use the [authService] to get a valid token.
 * That could be cached (if valid) or will fetch a new one. Please see how that actually works.
 */
class RemoteTransactionsRepositoryImpl(
    private val backendApi: BackendApi,
    private val authService: AuthService
) :
    RemoteTransactionsRepository {

    override suspend fun getTransactions(accountId: Int): List<Transaction> =
        backendApi.fetchTransactions(authService.getUserToken(APP_TOKEN).accessToken, accountId)
            .map { it.toDomainModel() }


    override suspend fun addTransaction(transaction: Transaction) {
        backendApi.addTransaction(authService.getUserToken(APP_TOKEN).accessToken, transaction.toDTO())
    }
}