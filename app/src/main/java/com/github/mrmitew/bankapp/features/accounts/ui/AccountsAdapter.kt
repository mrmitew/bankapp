package com.github.mrmitew.bankapp.features.accounts.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mrmitew.bankapp.R
import com.github.mrmitew.bankapp.features.accounts.vo.Account

class AccountsAdapter : ListAdapter<AccountViewItem, AccountsAdapter.BaseAccountViewHolder>(
    AccountViewItem.DIFF_CALLBACK
) {
    interface OnAccountClickListener {
        fun onAccountClick(account: Account)
    }

    abstract class BaseAccountViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bindTo(item: AccountViewItem)
    }

    inner class HeaderViewHolder(private val view: View) : BaseAccountViewHolder(view) {
        override fun bindTo(item: AccountViewItem) {
            val header = item as AccountViewItem.HeaderViewItem
            view.findViewById<TextView>(R.id.tv_name).text = header.title
            view.findViewById<TextView>(R.id.tv_currency).text = header.currency
        }
    }

    inner class AccountViewHolder(private val view: View) : BaseAccountViewHolder(view) {
        override fun bindTo(item: AccountViewItem) {
            val account = (item as AccountViewItem.AccountDecoratedViewItem).account
            view.findViewById<ViewGroup>(R.id.vg_item).setOnClickListener {
                listener?.onAccountClick(account)
            }
            view.findViewById<TextView>(R.id.tv_name).text = account.name
            view.findViewById<TextView>(R.id.tv_iban).text = account.iban
        }
    }

    var listener: OnAccountClickListener? = null

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is AccountViewItem.HeaderViewItem -> 0
        is AccountViewItem.AccountDecoratedViewItem -> 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseAccountViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (getItemViewType(viewType)) {
            0 -> HeaderViewHolder(inflater.inflate(R.layout.item_account_type, parent, false))
            1 -> AccountViewHolder(inflater.inflate(R.layout.item_account, parent, false))
            else -> throw IllegalArgumentException("Unsupported view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseAccountViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }
}