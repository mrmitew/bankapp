package com.github.mrmitew.bankapp.features.transactions.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mrmitew.bankapp.R
import com.github.mrmitew.bankapp.features.transactions.vo.Transaction
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class TransactionsOverviewFragment : Fragment() {
    private val args: TransactionsOverviewFragmentArgs by navArgs()
    private val viewModel: TransactionsViewModel by viewModel { parametersOf(args.account) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transactions_overview, container, false)

        view.findViewById<TextView>(R.id.tv_name).text = args.account.name
        view.findViewById<TextView>(R.id.tv_description).text = args.account.iban

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_transactions)
        val transactionsAdapter = TransactionsAdapter()

        recyclerView.adapter = transactionsAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        viewModel.transactionListStream.observe(viewLifecycleOwner, Observer {
            println("Submitting: $it")
            transactionsAdapter.submitList(it)
        })


        viewModel.accountBalanceStream.observe(viewLifecycleOwner, Observer {
            view.findViewById<TextView>(R.id.tv_amount).text = it
        })

        return view
    }
}

class TransactionsAdapter :
    ListAdapter<Transaction, TransactionsAdapter.TransactionViewHolder>(Transaction.DIFF_CALLBACK) {

    inner class TransactionViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bindTo(item: Transaction) {
            view.findViewById<TextView>(R.id.tv_target).text = item.targetName
            view.findViewById<TextView>(R.id.tv_amount).text = item.amount.toPlainString()
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