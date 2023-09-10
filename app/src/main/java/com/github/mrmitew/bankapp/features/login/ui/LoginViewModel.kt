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

    /**
     * Executes a login use case in a coroutine, scoped to the view model,
     * but the result will be awaited in a different scope (lifecycle scope of a fragment).
     * This way we can immediately return a result, instead of creating a
     * separate live data stream just to return the result.
     */
    suspend fun login(pinCode: CharArray): Result<User> {
        val deferredResult = CompletableDeferred<Result<User>>()

        viewModelScope.launch {
            deferredResult.complete(logInUserUseCase(pinCode))
        }

        return deferredResult.await()
    }
}
