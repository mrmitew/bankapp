package com.github.mrmitew.bankapp.features.accounts.repository.internal

import com.github.mrmitew.bankapp.APP_TOKEN
import com.github.mrmitew.bankapp.features.accounts.dto.toDomainModel
import com.github.mrmitew.bankapp.features.accounts.repository.RemoteAccountsRepository
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.auth.AuthService
import com.github.mrmitew.bankapp.features.backend.BackendApi
import com.github.mrmitew.bankapp.features.users.vo.User
import java.math.BigDecimal

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