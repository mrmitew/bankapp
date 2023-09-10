package com.github.mrmitew.bankapp.features.accounts.repository.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import com.github.mrmitew.bankapp.features.accounts.entity.toDomainModel
import com.github.mrmitew.bankapp.features.accounts.entity.toEntity
import com.github.mrmitew.bankapp.features.accounts.repository.LocalAccountsRepository
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.users.vo.User
import java.math.BigDecimal

/**
 * Implementation of a [LocalAccountsRepository] that uses Room database to manage
 * bank accounts.
 *
 * Entities are being mapped to domain models, so that we can mock or provide a fake
 * in tests (or in debug builds).
 */
class LocalAccountsRepositoryImpl(private val accountDao: AccountDao) :
    LocalAccountsRepository {
    override suspend fun getAccountsRefreshing(user: User): LiveData<List<Account>> {
        return accountDao.getAccountsRefreshing(user.id).distinctUntilChanged().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun storeAccounts(user: User, accounts: List<Account>) {
        accountDao.addAccounts(accounts.map { it.toEntity(user.id) })
    }

    override suspend fun deleteAccounts(user: User) {
        accountDao.deleteAccounts(user.id)
    }

    override suspend fun getAccountBalanceRefreshing(accountId: Int): LiveData<BigDecimal> {
        return accountDao.getAccountBalanceRefreshing(accountId)
    }

    override suspend fun updateAccountBalance(accountId: Int, newBalance: BigDecimal) {
        accountDao.updateAccountBalance(accountId, newBalance)
    }

    override suspend fun getAccounts(user: User): List<Account> =
        accountDao.getAccounts(user.id).map { it.toDomainModel() }
}
