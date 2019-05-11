package com.github.mrmitew.bankapp.features.login.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.github.mrmitew.bankapp.R
import com.github.mrmitew.bankapp.features.common.util.onClick
import com.github.mrmitew.bankapp.features.users.usecase.LogInUserUseCase
import com.github.mrmitew.bankapp.features.users.vo.User
import com.github.mrmitew.bankapp.features.users.usecase.Username
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LoginViewModel(private val logInUserUseCase: LogInUserUseCase) : ViewModel() {
    suspend fun login(username: Username): Result<User> {
        val deferredResult = CompletableDeferred<Result<User>>()

        viewModelScope.launch {
            deferredResult.complete(logInUserUseCase(username))
        }

        return deferredResult.await()
    }
}

class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val usernameView = view.findViewById<EditText>(R.id.et_username)

        view.findViewById<Button>(R.id.btn_login).onClick(viewLifecycleOwner) {
            if (usernameView.text.isNotEmpty()) {
                viewModel.login(usernameView.text.toString()).fold({
                    findNavController().navigate(R.id.action_account_overview)
                }, { e ->
                    Toast.makeText(this@LoginFragment.context, "An error occurred", Toast.LENGTH_SHORT).show()
                    Log.e("LoginFragment", e.message, e)
                })
            }
        }

        return view
    }

}