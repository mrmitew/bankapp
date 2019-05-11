package com.github.mrmitew.bankapp.features.accounts.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.github.mrmitew.bankapp.features.accounts.usecase.GetUserAccountsUseCase
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.common.usecase.invoke
import com.github.mrmitew.bankapp.features.common.vo.getOrNull
import kotlinx.coroutines.cancel

class AccountListViewModel(private val getUserAccountUseCase: GetUserAccountsUseCase) : ViewModel() {
    private val accountItemListStream: LiveData<List<AccountViewItem>>

    init {
        accountItemListStream = liveData {
            getUserAccountUseCase().getOrNull()?.let { liveDataSource ->
                emitSource(liveDataSource.map { it.toUiModel() })
            }
        }
    }

    fun getAccountItemList() = accountItemListStream

    override fun onCleared() {
        super.onCleared()
        getUserAccountUseCase.cancel()
    }

    private fun List<Account>.toUiModel(): List<AccountViewItem> {
        // We can write this function much smarter, but didn't want to complicate things.

        val accountItems = mutableListOf<AccountViewItem>()

        val paymentAccounts =
            filter { it.type == Account.TYPE_PAYMENT }.map { it.toAccountDecoratedItem() }

        val savingAccounts =
            filter { it.type == Account.TYPE_SAVINGS }.map { it.toAccountDecoratedItem() }

        if (paymentAccounts.isNotEmpty()) {
            accountItems.add(
                AccountViewItem.HeaderViewItem(
                    title = "Payment accounts",
                    // Assuming that payment accounts are always the same currency,
                    // otherwise we need to modify the layout
                    currency = paymentAccounts[0].account.currency
                )
            )
            accountItems.addAll(paymentAccounts)
        }

        if (savingAccounts.isNotEmpty()) {
            accountItems.add(
                AccountViewItem.HeaderViewItem(
                    title = "Saving accounts",
                    // Assuming that payment accounts are always the same currency,
                    // otherwise we need to modify the layout
                    currency = savingAccounts[0].account.currency
                )
            )
            accountItems.addAll(savingAccounts)
        }

        return accountItems
    }
}