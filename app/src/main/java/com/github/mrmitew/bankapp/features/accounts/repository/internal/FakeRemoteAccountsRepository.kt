package com.github.mrmitew.bankapp.features.accounts.repository.internal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.github.mrmitew.bankapp.features.accounts.repository.AccountsRepository
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.users.vo.User
import kotlinx.coroutines.delay
import java.math.BigDecimal

class FakeRemoteAccountsRepository : AccountsRepository {
    private var accountBalance: BigDecimal = BigDecimal(1_992)
    private val accountBalanceStream = MutableLiveData<BigDecimal>(accountBalance)

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

    override suspend fun deleteAccounts(user: User) {
        // no-op
    }

    override suspend fun getAccountBalance(accountId: Int): LiveData<BigDecimal> {
        return accountBalanceStream
    }

    internal fun updateAccountBalance(newBalance: BigDecimal) {
        accountBalance = newBalance
        accountBalanceStream.value = accountBalance
    }
}