package com.github.mrmitew.bankapp.features.accounts.repository.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mrmitew.bankapp.features.accounts.repository.RemoteAccountsRepository
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.users.vo.User
import kotlinx.coroutines.delay
import java.math.BigDecimal

// TODO: Fake that we are calling a backend, just like with the other FakeRemote*Repositories
class FakeRemoteAccountsRepository : RemoteAccountsRepository {
    private val accounts = mutableListOf(
        Account(1, "Stefan Mitev", "123", Account.TYPE_PAYMENT, "EUR", BigDecimal(1_992)),
        Account(2, "Stefan Mitev", "456", Account.TYPE_SAVINGS, "EUR", BigDecimal(9_090))
    )

    override suspend fun getAccounts(user: User): LiveData<List<Account>> {
        val mutableLiveData =
            MutableLiveData<List<Account>>(fetchAccounts())
        println("Fetched ${mutableLiveData.value}")
        return mutableLiveData
    }

    private suspend fun fetchAccounts(): List<Account> {
        delay(1000)
        return accounts
    }

    override suspend fun storeAccounts(user: User, accounts: List<Account>) {
        // no-op
    }

    override suspend fun deleteAccounts(user: User) {
        // no-op
    }

    override suspend fun updateAccountBalance(accountId: Int, newBalance: BigDecimal) {
        when (accountId) {
            1 -> accounts[0] = accounts[0].copy(balance = newBalance)
            2 -> accounts[1] = accounts[1].copy(balance = newBalance)
            else -> throw IllegalArgumentException()
        }
    }

    override suspend fun fetchAccountBalance(accountId: Int): BigDecimal {
        return when (accountId) {
            1 -> accounts[0].balance
            2 -> accounts[1].balance
            else -> throw IllegalArgumentException()
        }
    }
}