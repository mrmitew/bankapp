package com.github.mrmitew.bankapp.features.transactions.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.github.mrmitew.bankapp.R
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import java.util.Locale

class AccountsSpinnerAdapter(private val accounts: List<Account>) : BaseAdapter() {

    inner class AccountViewHolder(private val view: View) {
        fun bindTo(account: Account) {
            view.findViewById<TextView>(R.id.tv_name).text = account.name
            view.findViewById<TextView>(R.id.tv_iban).text = account.iban
            view.findViewById<TextView>(R.id.tv_balance).text =
                String.format(Locale.getDefault(), "%s", account.balance)
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val account = getItem(position)

        // The old-school view holder pattern
        val viewHolder: AccountViewHolder
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_account, parent, false)
            viewHolder = AccountViewHolder(view)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as AccountViewHolder
        }

        viewHolder.bindTo(account)

        return view!!
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getView(position, convertView, parent!!)
    }

    override fun getItemId(position: Int): Long = position.toLong()
    override fun getCount(): Int = accounts.size
    override fun getItem(position: Int): Account = accounts[position]
}