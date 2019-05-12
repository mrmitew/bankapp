package com.github.mrmitew.bankapp.features.accounts.repository.internal

import com.github.mrmitew.bankapp.features.accounts.dto.toDomainModel
import com.github.mrmitew.bankapp.features.accounts.repository.RemoteAccountsRepository
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.users.repository.BackendApi
import com.github.mrmitew.bankapp.features.users.vo.User
import java.math.BigDecimal

class RemoteAccountsRepositoryImpl(private val backendApi: BackendApi) : RemoteAccountsRepository {
    override suspend fun fetchAccounts(user: User): List<Account> =
        backendApi.fetchAccounts(user).map { it.toDomainModel() }

    override suspend fun updateAccountBalance(accountId: Int, newBalance: BigDecimal) =
        backendApi.updateAccountBalance(accountId, newBalance)

    override suspend fun fetchAccountBalance(accountId: Int): BigDecimal =
        backendApi.fetchAccountBalance(accountId)
}