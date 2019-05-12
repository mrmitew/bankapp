package com.github.mrmitew.bankapp.features.login.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.mrmitew.bankapp.R
import com.github.mrmitew.bankapp.features.common.util.onClick
import com.github.mrmitew.bankapp.features.common.vo.fold
import com.github.mrmitew.bankapp.features.users.usecase.WrongPasswordException
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModel()
    private lateinit var pinCode: CharArray

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val pincodeView = view.findViewById<EditText>(R.id.et_pincode)

        view.findViewById<Button>(R.id.btn_login)
            // We use a coroutine actor to prevent accidental multiple taps while we are trying to login
            .onClick(viewLifecycleOwner) {
                pinCode = pincodeView.text.toString().toCharArray()
                if (pincodeView.text.isNotEmpty()) {
                    doLoginWith(pinCode)
                }
            }

        return view
    }

    private suspend fun doLoginWith(pinCode: CharArray) {
        viewModel.login(pinCode).fold({
            findNavController().navigate(LoginFragmentDirections.actionAccountOverview())
            Unit
        }, { e ->
            if (e is WrongPasswordException) {
                Toast.makeText(this@LoginFragment.context, "Wrong password", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@LoginFragment.context, "An error occurred", Toast.LENGTH_SHORT).show()
            }
        })
    }

}