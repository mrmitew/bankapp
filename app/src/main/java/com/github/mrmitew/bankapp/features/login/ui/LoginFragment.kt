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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import com.commonsware.cwac.saferoom.SQLCipherUtils
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.github.mrmitew.bankapp.R
import com.github.mrmitew.bankapp.features.common.database.AppDatabase
import com.github.mrmitew.bankapp.features.common.util.onClick
import com.github.mrmitew.bankapp.features.users.usecase.LogInUserUseCase
import com.github.mrmitew.bankapp.features.users.usecase.Username
import com.github.mrmitew.bankapp.features.users.vo.User
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import net.sqlcipher.database.SQLiteOpenHelper
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LoginViewModel(
    private val logInUserUseCase: LogInUserUseCase
) : ViewModel() {
    suspend fun login(username: Username): Result<User> {
        val deferredResult = CompletableDeferred<Result<User>>()

        viewModelScope.launch {
            deferredResult.complete(logInUserUseCase(username))
        }

        return deferredResult.await()
    }
}

class LoginFragment : Fragment() {
    private val viewModel: LoginViewModel by viewModel()
    private lateinit var pinCode: CharArray

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        val pincodeView = view.findViewById<EditText>(R.id.et_username)

        view.findViewById<Button>(R.id.btn_login).onClick(viewLifecycleOwner) {
            pinCode = pincodeView.text.toString().toCharArray()

            try {
                if (pincodeView.text.isNotEmpty()) {
                    doLoginWith(pinCode)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@LoginFragment.context, "Wrong password", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private suspend fun doLoginWith(pinCode: CharArray) {
        viewModel.login("test").fold({
            findNavController().navigate(R.id.action_account_overview)
        }, { e ->
            Toast.makeText(this@LoginFragment.context, "An error occurred", Toast.LENGTH_SHORT).show()
            Log.e("LoginFragment", e.message, e)
        })
    }

}