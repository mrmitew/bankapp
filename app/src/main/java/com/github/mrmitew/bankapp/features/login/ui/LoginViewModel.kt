package com.github.mrmitew.bankapp.features.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mrmitew.bankapp.features.common.vo.Result
import com.github.mrmitew.bankapp.features.users.usecase.LogInUserUseCase
import com.github.mrmitew.bankapp.features.users.vo.User
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class LoginViewModel(
    private val logInUserUseCase: LogInUserUseCase
) : ViewModel() {
    suspend fun login(pinCode: CharArray): Result<User> {
        val deferredResult =
            CompletableDeferred<Result<User>>()

        viewModelScope.launch {
            deferredResult.complete(logInUserUseCase(pinCode))
        }

        return deferredResult.await()
    }
}