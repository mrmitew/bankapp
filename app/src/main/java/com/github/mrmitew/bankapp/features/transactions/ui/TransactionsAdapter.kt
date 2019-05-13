package com.github.mrmitew.bankapp.features.transactions.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mrmitew.bankapp.R
import com.github.mrmitew.bankapp.features.transactions.vo.Transaction

class TransactionsAdapter :
    ListAdapter<Transaction, TransactionsAdapter.TransactionViewHolder>(
        Transaction.DIFF_CALLBACK
    ) {

    class TransactionViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bindTo(item: Transaction) {
            view.findViewById<TextView>(R.id.tv_target).text = item.name
            view.findViewById<TextView>(R.id.tv_balance).text = item.amount.toPlainString()
            view.findViewById<TextView>(R.id.tv_description).text =
                if (item.comment != null && item.comment.isNotEmpty()) item.comment else item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder =
        TransactionViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_transaction, parent, false)
        )

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }
}