package com.github.mrmitew.bankapp.features.accounts.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        viewModel.getAccountItemList().observe(viewLifecycleOwner, Observer {
            projectsAdapter.submitList(it)
            println("Received: $it")
        })

        return view
    }

    override fun onAccountClick(account: Account) {
        // Request navigation
        findNavController().navigate(
            AccountListFragmentDirections.actionTransactionDetails(
                account
            )
        )
    }
}