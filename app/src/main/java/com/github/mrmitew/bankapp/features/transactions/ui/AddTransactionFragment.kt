package com.github.mrmitew.bankapp.features.transactions.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mrmitew.bankapp.R
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.common.util.onClick
import com.github.mrmitew.bankapp.features.common.vo.catchResult
import com.github.mrmitew.bankapp.features.common.vo.getOrDefault
import com.github.mrmitew.bankapp.features.common.vo.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.math.BigDecimal
import java.util.Locale


class AddTransactionFragment : Fragment() {
    private val args: AddTransactionFragmentArgs by navArgs()
    private val viewModel: AddTransactionViewModel by viewModel { parametersOf(args.account) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_add_transaction, container, false)

        val fromToLabel = if (!args.isDeposit)
            getString(R.string.to) else getString(R.string.from)

        val message = view.findViewById<TextView>(R.id.et_message)
        val amount = view.findViewById<TextView>(R.id.et_amount)
        val spinner = view.findViewById<Spinner>(R.id.sp_target_account)

//        val list = ArrayList<String>()
//        list.add("list 1")
//        list.add("list 2")
//        list.add("list 3")
//        val dataAdapter = ArrayAdapter<String>(
//            requireContext(),
//            android.R.layout.simple_spinner_item, list
//        )
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinner.adapter = dataAdapter

        view.findViewById<TextView>(R.id.tv_from_to).text = fromToLabel
        view.findViewById<TextView>(R.id.tv_name).text = args.account.name
        view.findViewById<TextView>(R.id.tv_iban).text = args.account.iban
        view.findViewById<TextView>(R.id.tv_balance).text =
            String.format(Locale.getDefault(), "%s", args.account.balance)

        // Use the lifecycleScope that was released after Google I/O 2019.
        // Allows us to scope a coroutine within the lifecyle of the fragment,
        // which will cancel all coroutines if the fragment stops
        lifecycleScope.launch {
            val accounts = viewModel.getAvailableAccountsForTransaction()
            val adapter = AccountsSpinnerAdapter(accounts)
            withContext(Dispatchers.Main) {
                spinner.adapter = adapter
            }
        }

        view.findViewById<Button>(R.id.btn_submit)
            .onClick(viewLifecycleOwner) {
                // Since we are going to perform a fragment transaction, we must be sure to
                // execute it only after the fragment has gone through its "STARTED' state.
                // Though.. it shouldn't be a problem even if we are using the "launch" builder
                // since we are creating the coroutine in [onCreateView] already.
                lifecycleScope.launchWhenStarted {
                    catchResult {
                        viewModel.performTransaction(
                            args.isDeposit,
                            (spinner.selectedItem as Account),
                            message.text.toString(),
                            catchResult { BigDecimal(amount.text.toString()) }
                                .getOrDefault(BigDecimal.ZERO)
                        )
                    }.onSuccess {
                        findNavController().popBackStack()
                    }
                }
            }

        return view
    }
}

