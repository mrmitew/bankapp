package com.github.mrmitew.bankapp.features.accounts.repository.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.github.mrmitew.bankapp.features.accounts.entity.AccountEntity
import com.github.mrmitew.bankapp.features.accounts.repository.AccountDao
import com.github.mrmitew.bankapp.features.accounts.repository.AccountsRepository
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.common.util.distinctUntilChanged
import com.github.mrmitew.bankapp.features.users.vo.User

class LocalAccountsRepository(private val accountDao: AccountDao) :
    AccountsRepository {
    override suspend fun getAccounts(user: User): LiveData<List<Account>> {
        return Transformations.map(accountDao.getAccounts(user.id).distinctUntilChanged()) { entities ->
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
}