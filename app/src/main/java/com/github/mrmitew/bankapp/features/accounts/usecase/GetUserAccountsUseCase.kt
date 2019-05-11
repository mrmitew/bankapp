package com.github.mrmitew.bankapp.features.accounts.usecase

import androidx.lifecycle.LiveData
import com.github.mrmitew.bankapp.features.accounts.repository.AccountsRepository
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.common.usecase.UseCase
import com.github.mrmitew.bankapp.features.users.repository.LocalUsersRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class GetUserAccountsUseCase(
    private val localUsersRepository: LocalUsersRepository,
    private val localAccountsRepository: AccountsRepository,
    private val remoteAccountsRepository: AccountsRepository
) : UseCase<Unit, Result<LiveData<List<Account>>>>, CoroutineScope by MainScope() {
    override suspend fun invoke(param: Unit) = runCatching {
        val user = localUsersRepository.getLoggedInUser()!!

        // Do a fetch and store locally in background
        // We expect that the database will notify us when it has changed

        launch {
            runCatching {
                // Fetch from network
                val projects = remoteAccountsRepository.getAccounts(user)
                if (projects.value!!.isNotEmpty()) {
                    // Store to disk
                    localAccountsRepository.storeAccounts(user, projects.value!!)
                }
            }.onFailure {
                // Log
                it.printStackTrace()
            }
        }

        // Always return from local database
        return@runCatching localAccountsRepository.getAccounts(user)
    }
}