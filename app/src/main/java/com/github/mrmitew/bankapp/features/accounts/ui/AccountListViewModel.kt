package com.github.mrmitew.bankapp.features.accounts.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.github.mrmitew.bankapp.features.accounts.usecase.FetchUserAccountsUseCase
import com.github.mrmitew.bankapp.features.accounts.usecase.RefreshUserAccountsUseCase
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.common.usecase.invoke
import kotlinx.coroutines.launch

class AccountListViewModel(
    private val refreshUserAccountUseCase: RefreshUserAccountsUseCase,
    private val fetchUserAccountsUseCase: FetchUserAccountsUseCase
) : ViewModel() {
    private val accountItemListStream: LiveData<List<AccountViewItem>>
    val loadingStateStream: LiveData<LoadingState> = MutableLiveData(LoadingState.INITIAL_STATE)

    data class LoadingState(
        val isRefreshing: Boolean,
        val isInitialLoading: Boolean
    ) {
        companion object {
            internal val INITIAL_STATE = LoadingState(
                isRefreshing = false,
                isInitialLoading = true
            )
        }
    }

    init {
        // Create a coroutine live data (https://developer.android.com/topic/libraries/architecture/coroutines)
        // Query the business logic to get user bank accounts
        // And then map them to something that the UI can render
        accountItemListStream = liveData {
            emitSource(fetchUserAccountsUseCase()!!.map {
                it.toUiModel().also {
                    emitInitialLoadingCompleted()
                }
            })
        }
    }

    fun getAccountItemList() = accountItemListStream

    /**
     * This will trigger fetch of user bank accounts and will store them into database
     * We'll do nothing here, because we are already observing a livedata stream from the local store/repostiory
     * in the init block above
     */
    fun refreshAccounts() = viewModelScope.launch {
        emitRefreshing(hasCompleted = false)
        refreshUserAccountUseCase()
        emitRefreshing(hasCompleted = true)
    }

    private fun emitInitialLoadingCompleted() =
        (loadingStateStream as MutableLiveData).postValue(loadingStateStream.value!!.copy(isInitialLoading = false))

    private fun emitRefreshing(hasCompleted: Boolean) =
        (loadingStateStream as MutableLiveData).postValue(loadingStateStream.value!!.copy(isRefreshing = !hasCompleted))

    /**
     * Convert the list of [Account] to a UI model that can be directly rendered by the View.
     * In this case, it will be used by a RecyclerView adapter.
     */
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