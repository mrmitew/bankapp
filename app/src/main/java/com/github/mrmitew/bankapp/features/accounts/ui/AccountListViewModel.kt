package com.github.mrmitew.bankapp.features.accounts.ui

import androidx.lifecycle.*
import com.github.mrmitew.bankapp.features.accounts.usecase.RefreshUserAccountsUseCase
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.common.usecase.invoke
import com.github.mrmitew.bankapp.features.common.vo.getOrNull
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AccountListViewModel(private val refreshUserAccountUseCase: RefreshUserAccountsUseCase) : ViewModel() {
    private val accountItemListStream: LiveData<List<AccountViewItem>>

    init {
        // Create a coroutine live data (https://developer.android.com/topic/libraries/architecture/coroutines)
        // Query the business logic to get user bank accounts
        // And then map them to something that the UI can render
        accountItemListStream = liveData {
            emitSource(refreshUserAccountUseCase()!!.map { it.toUiModel() })
        }
    }

    fun getAccountItemList() = accountItemListStream

    /**
     * This will trigger fetch of user bank accounts and will store them into database
     * We'll do nothing here, because we are already observing a livedata stream from the database
     * in the init above
     */
    fun refreshAccounts() = viewModelScope.launch { refreshUserAccountUseCase() }

    override fun onCleared() {
        super.onCleared()
        refreshUserAccountUseCase.cancel()
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