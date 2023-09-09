package com.github.mrmitew.bankapp.features.transactions.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mrmitew.bankapp.R
import com.github.mrmitew.bankapp.features.transactions.vo.Transaction

class TransactionsAdapter :
    PagingDataAdapter<Transaction, TransactionsAdapter.TransactionViewHolder>(
        Transaction.DIFF_CALLBACK
    ) {

    class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var description: TextView = view.findViewById(R.id.tv_description)
        private var balance: TextView = view.findViewById(R.id.tv_balance)
        private var target: TextView = view.findViewById(R.id.tv_target)

        fun bindTo(item: Transaction?) {
            if (item != null) {
                target.text = item.name
                balance.text = item.amount.toPlainString()
                description.text =
                    if (item.comment != null && item.comment.isNotEmpty()) item.comment else item.description
            } else {
                target.text = "???"
                balance.text = "?"
                description.text = "????"
            }
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