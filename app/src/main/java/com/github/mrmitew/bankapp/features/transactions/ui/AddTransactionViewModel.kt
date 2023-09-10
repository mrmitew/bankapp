package com.github.mrmitew.bankapp.features.transactions.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import java.math.BigDecimal

class AddTransactionViewModel(
    private val account: Account,
    private val getAvailableAccountsForTransactionUseCase: GetAvailableAccountsForTransactionUseCase,
    private val addTransactionUseCase: AddTransactionUseCase
) : ViewModel() {
    /**
     * We'll basically get all user accounts without the currently selected one
     */
    suspend fun getAvailableAccountsForTransaction(): List<Account> {
        val deferredList =
            CompletableDeferred<List<Account>>()

        viewModelScope.launch {
            deferredList.complete(getAvailableAccountsForTransactionUseCase(account))
        }

        return deferredList.await()
    }

    suspend fun performTransaction(
        isDeposit: Boolean,
        targetAccount: Account,
        comment: String?,
        amount: BigDecimal
    ) {
        addTransactionUseCase(
            AddTransactionDTO(
                sourceAccount = account,
                targetAccount = targetAccount,
                isDeposit = isDeposit,
                comment = comment,
                amount = amount
            )
        )
    }
}
