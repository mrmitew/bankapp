package com.github.mrmitew.bankapp.features.accounts.repository.internal

import com.github.mrmitew.bankapp.features.accounts.dto.toDomainModel
import com.github.mrmitew.bankapp.features.accounts.repository.RemoteAccountsRepository
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.auth.AuthService
import com.github.mrmitew.bankapp.features.auth.di.APP_TOKEN
import com.github.mrmitew.bankapp.features.backend.BackendApi
import com.github.mrmitew.bankapp.features.users.vo.User
import java.math.BigDecimal

/**
 * Accounts repository that works with a remote data source.
 * [backendApi] can be a Retrofit instance here.. but it isn't.
 * We faked it.. Though its just an interface.. only the dependency
 * injection framework knows the actual implementation
 *
 * Every call requires a user token, so we'll use the [authService] to get a valid token.
 * That could be cached (if valid) or will fetch a new one. Please see how that actually works.
 */
class RemoteAccountsRepositoryImpl(
    private val backendApi: BackendApi,
    private val authService: AuthService
) : RemoteAccountsRepository {
    override suspend fun fetchAccounts(user: User): List<Account> =
        backendApi.fetchAccounts(authService.getUserToken(APP_TOKEN).accessToken, user).map { it.toDomainModel() }

    override suspend fun updateAccountBalance(accountId: Int, newBalance: BigDecimal) =
        backendApi.updateAccountBalance(authService.getUserToken(APP_TOKEN).accessToken, accountId, newBalance)

    override suspend fun fetchAccountBalance(accountId: Int): BigDecimal =
        backendApi.fetchAccountBalance(authService.getUserToken(APP_TOKEN).accessToken, accountId)
}