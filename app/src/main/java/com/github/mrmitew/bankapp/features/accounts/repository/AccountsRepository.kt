package com.github.mrmitew.bankapp.features.accounts.repository

import androidx.lifecycle.LiveData
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.users.vo.User
import java.math.BigDecimal

/**
 * Created by Stefan Mitev on 4-5-19.
 */

interface AccountsRepository {
    suspend fun getAccounts(user: User): LiveData<List<Account>>
    suspend fun storeAccounts(user: User, accounts: List<Account>)
    suspend fun deleteAccounts(user: User)
    suspend fun getAccountBalance(accountId: Int): LiveData<BigDecimal>
}