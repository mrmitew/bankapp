package com.github.mrmitew.bankapp.features.accounts.repository.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mrmitew.bankapp.features.accounts.repository.AccountsRepository
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.users.vo.User
import kotlinx.coroutines.delay

class FakeRemoteAccountsRepository : AccountsRepository {
    private val accounts = listOf(
        Account(1, "Stefan Mitev", "123", Account.TYPE_PAYMENT, "EUR"),
        Account(2, "Stefan Mitev", "456", Account.TYPE_SAVINGS, "EUR")
    )

    override suspend fun getAccounts(user: User): LiveData<List<Account>> {
        val mutableLiveData =
            MutableLiveData<List<Account>>()
        mutableLiveData.value = fetchAccounts()
        println("Fetched ${mutableLiveData.value}")
        return mutableLiveData
    }

    private suspend fun fetchAccounts(): List<Account> {
        delay(3000)
        return accounts
    }

    override suspend fun storeAccounts(user: User, accounts: List<Account>) {
        // no-op
    }
}