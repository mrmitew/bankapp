package com.github.mrmitew.bankapp.features.transactions.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.github.mrmitew.bankapp.R


class TransactionsOverviewFragment : Fragment() {
    private val args: TransactionsOverviewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transactions_overview, container, false)

        view.findViewById<TextView>(R.id.tv_name).text = args.account.name
        view.findViewById<TextView>(R.id.tv_description).text = args.account.iban

        return view
    }

}
