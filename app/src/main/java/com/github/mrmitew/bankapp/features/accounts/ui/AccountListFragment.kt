package com.github.mrmitew.bankapp.features.accounts.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mrmitew.bankapp.R
import com.github.mrmitew.bankapp.features.accounts.usecase.GetUserAccountsUseCase
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.common.usecase.invoke
import kotlinx.coroutines.cancel
import org.koin.androidx.viewmodel.ext.android.viewModel


class AccountListViewModel(private val getUserAccountUseCase: GetUserAccountsUseCase) : ViewModel() {
    private val accounts: LiveData<List<Account>>

    init {
        accounts = liveData {
            emitSource(requireNotNull(getUserAccountUseCase()).getOrThrow())
        }
    }

    fun getAccounts() = accounts

    override fun onCleared() {
        super.onCleared()
        getUserAccountUseCase.cancel()
    }
}

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

        viewModel.getAccounts().observe(viewLifecycleOwner, Observer {
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

class AccountsAdapter : ListAdapter<Account, AccountsAdapter.AccountViewHolder>(
    Account.DIFF_CALLBACK) {
    interface OnAccountClickListener {
        fun onAccountClick(account: Account)
    }

    inner class AccountViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bindTo(item: Account) {
            view.findViewById<ViewGroup>(R.id.vg_item).setOnClickListener {
                listener?.onAccountClick(item)
            }
            view.findViewById<TextView>(R.id.tv_name).text = item.name
            view.findViewById<TextView>(R.id.tv_iban).text = item.iban
        }
    }

    var listener: OnAccountClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder =
        AccountViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_account, parent, false))

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }
}