package com.github.mrmitew.bankapp.features.accounts.ui

import androidx.recyclerview.widget.DiffUtil
import com.github.mrmitew.bankapp.features.accounts.vo.Account

/**
 * Created by Stefan Mitev on 11-5-19.
 */

/**
 * Data structure that represents an account or header item in a RecyclerView
 */
sealed class AccountViewItem {
    data class HeaderViewItem(val title: String, val currency: String) : AccountViewItem()
    data class AccountDecoratedViewItem(val account: Account) : AccountViewItem()

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AccountViewItem>() {
            override fun areItemsTheSame(oldItem: AccountViewItem, newItem: AccountViewItem): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: AccountViewItem, newItem: AccountViewItem): Boolean {
                return true
            }
        }
    }
}

fun Account.toAccountDecoratedItem() = AccountViewItem.AccountDecoratedViewItem(this)
