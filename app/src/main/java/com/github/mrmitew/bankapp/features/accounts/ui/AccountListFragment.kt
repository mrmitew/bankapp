package com.github.mrmitew.bankapp.features.accounts.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mrmitew.bankapp.R
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.common.vo.catchResult
import com.github.mrmitew.bankapp.features.common.vo.getOrNull
import com.github.mrmitew.bankapp.features.common.vo.onFailure
import com.github.mrmitew.bankapp.features.common.vo.onSuccess
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.IllegalArgumentException

class AccountListFragment : Fragment(),
    AccountsAdapter.OnAccountClickListener {
    private val viewModel: AccountListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_accounts, container, false)
        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.vg_swipeToRefresh)

        viewModel.loadingStateStream.observe(viewLifecycleOwner, Observer {
            // Keep the loading if we haven't completely finished fetching data
            swipeRefreshLayout.isRefreshing = it.isRefreshing || it.isInitialLoading
        })

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_accounts)
        val accountsAdapter = AccountsAdapter()

        accountsAdapter.listener = this
        recyclerView.adapter = accountsAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshAccounts()
        }

        viewModel.getAccountItemList().observe(viewLifecycleOwner, Observer {
            println("Received $it")
            accountsAdapter.submitList(it)
        })

        setHasOptionsMenu(true)

        return view
    }

    override fun onAccountClick(account: Account) {
        catchResult {
            when (account.type) {
                Account.TYPE_PAYMENT -> AccountListFragmentDirections.actionTransactionsOverview(account)
                // TODO: Navigate to a different fragment
                Account.TYPE_SAVINGS -> AccountListFragmentDirections.actionSavingsAccountTransactionsOverview(account)
                else -> throw IllegalArgumentException()
            }
        }
            .onFailure {
                // TODO: Log
                it.printStackTrace()
            }
            .onSuccess {
                findNavController().navigate(it)
            }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.account_menu, menu)
        menu.findItem(R.id.refresh).setOnMenuItemClickListener {
            viewModel.refreshAccounts()
            true
        }
        super.onCreateOptionsMenu(menu, inflater)
    }
}