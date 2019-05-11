package com.github.mrmitew.bankapp.features.accounts.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mrmitew.bankapp.R
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountListFragment : Fragment(),
    AccountsAdapter.OnAccountClickListener {
    private val viewModel: AccountListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_accounts, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_accounts)
        val projectsAdapter = AccountsAdapter()

        projectsAdapter.listener = this
        recyclerView.adapter = projectsAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        // FYI, Database will emit using [distinctUntilChanged], so after refresh if accounts have not
        // been modified since last emission, the observer wouldn't be called
        viewModel.getAccountItemList().observe(viewLifecycleOwner, Observer {
            println("Received: $it")
            projectsAdapter.submitList(it)
        })

        setHasOptionsMenu(true)

        return view
    }

    override fun onAccountClick(account: Account) {
        // Request navigation
        findNavController().navigate(AccountListFragmentDirections.accountOverview(account))
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