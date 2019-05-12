package com.github.mrmitew.bankapp.features.accounts.repository.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import com.github.mrmitew.bankapp.features.accounts.entity.AccountEntity
import com.github.mrmitew.bankapp.features.accounts.repository.AccountDao
import com.github.mrmitew.bankapp.features.accounts.repository.LocalAccountsRepository
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.users.vo.User
import java.math.BigDecimal

class RoomAccountsRepository(private val accountDao: AccountDao) :
    LocalAccountsRepository {
    override suspend fun getAccounts(user: User): LiveData<List<Account>> {
        return accountDao.getAccounts(user.id).distinctUntilChanged().map { entities ->
            entities.map {
                Account(
                    it.id,
                    it.name,
                    it.iban,
                    it.type,
                    it.currency
                )
            }
        }
    }

    override suspend fun storeAccounts(user: User, accounts: List<Account>) {
        accountDao.addAccounts(accounts.map {
            AccountEntity(
                user.id,
                it.id,
                it.name,
                it.iban,
                it.type,
                it.currency
            )
        })
    }

    override suspend fun deleteAccounts(user: User) {
        accountDao.deleteAccounts(user.id)
    }

    override suspend fun getAccountBalance(accountId: Int): LiveData<BigDecimal> {
        throw UnsupportedOperationException()
    }
}