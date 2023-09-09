package com.github.mrmitew.bankapp.features.transactions.ui


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mrmitew.bankapp.R
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


/**
 * Transactions overview for Savings accounts
 */
class SavingsAccountTransactionsOverviewFragment : TransactionsOverviewFragment() {
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Make sure the account is a savings account
        require(args.account.type == Account.TYPE_SAVINGS)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        val withdrawButton = view.findViewById<TextView>(R.id.btn_left)
        val depositButton = view.findViewById<TextView>(R.id.btn_right)

        // We could also just inflate a different layout..but keeping it simple
        withdrawButton.text = getString(R.string.withdraw)
        depositButton.text = getString(R.string.deposit)

        withdrawButton.setOnClickListener {
            findNavController().navigate(
                SavingsAccountTransactionsOverviewFragmentDirections.addTransaction(/*isDeposit*/false, args.account)
            )
        }

        depositButton.setOnClickListener {
            findNavController().navigate(
                SavingsAccountTransactionsOverviewFragmentDirections.addTransaction(/*isDeposit*/true, args.account)
            )
        }

        return view
    }
}

/**
 * Transactions overview for Payment accounts
 */
open class TransactionsOverviewFragment : Fragment() {
    protected val args: TransactionsOverviewFragmentArgs by navArgs()
    private val viewModel: TransactionsViewModel by viewModel { parametersOf(args.account) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_transactions_overview, container, false)

        view.findViewById<TextView>(R.id.tv_name).text = args.account.name
        view.findViewById<TextView>(R.id.tv_description).text = args.account.iban
        view.findViewById<TextView>(R.id.tv_currency).text = args.account.currency

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_transactions)
        val transactionsAdapter = TransactionsAdapter()

        recyclerView.adapter = transactionsAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        viewModel.transactionListStream.observe(viewLifecycleOwner, Observer {
            viewLifecycleOwner.lifecycleScope.launch {
                transactionsAdapter.submitData(it)
            }
        })

        viewModel.accountBalanceStream.observe(viewLifecycleOwner, Observer {
            view.findViewById<TextView>(R.id.tv_balance).text = it
        })

        return view
    }
}

