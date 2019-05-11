package com.github.mrmitew.bankapp.features.accounts.usecase

import androidx.lifecycle.LiveData
import com.github.mrmitew.bankapp.features.accounts.repository.AccountsRepository
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.common.usecase.Cancellable
import com.github.mrmitew.bankapp.features.common.usecase.UseCase
import com.github.mrmitew.bankapp.features.common.usecase.UseCaseContextScope
import com.github.mrmitew.bankapp.features.common.vo.catchResult
import com.github.mrmitew.bankapp.features.common.vo.onFailure
import com.github.mrmitew.bankapp.features.users.repository.LocalUsersRepository
import kotlinx.coroutines.*
import java.io.Closeable
import kotlin.coroutines.CoroutineContext

/**
 * Use case that will return a stream with user's bank accounts taken from a local repository.
 * Meanwhile it will fetch accounts from a remote repository (backend) and it will update the local repository.
 * If the local repository changes, it will emit, so we don't need to do anything in addition.
 * We have a single source of truth, that is the local database. In a real example with a banking app, we wouldn't
 * be storing this kind of data on disk.
 *
 * Note that the class that can be unit tested as it has no Android related classes.
 * LiveData can still be unit tested under the JVM. See the test folder for an example.
 */
class RefreshUserAccountsUseCase(
    private val localUsersRepository: LocalUsersRepository,
    private val localAccountsRepository: AccountsRepository,
    private val remoteAccountsRepository: AccountsRepository
) : UseCase<Unit, LiveData<List<Account>>>, Cancellable {
    private val internalScope
        get() = // Normally, it should be .IO since we'll be doing disk operations,
            // but I haven't made mechanism to mock it in tests
            UseCaseContextScope(SupervisorJob() + Dispatchers.Main)

    override suspend fun invoke(param: Unit): LiveData<List<Account>> {
        val user = localUsersRepository.getLoggedInUser()!!

        // Do a fetch and store locally in background
        // We expect that the database will notify us when it has changed

        internalScope.launch {
            catchResult {
                // Fetch from network
                val projects = remoteAccountsRepository.getAccounts(user)
                if (projects.value!!.isNotEmpty()) {
                    // Store to disk
                    // TODO: We should make a diff and remove the accounts that have been deleted on the server
                    // This of course won't happen in our example since everything is deterministic.
                    localAccountsRepository.storeAccounts(user, projects.value!!)
                } else {
                    localAccountsRepository.deleteAccounts(user)
                }
            }.onFailure {
                // Log
                it.printStackTrace()
            }
        }

        // Always return from local database
        return localAccountsRepository.getAccounts(user)
    }

    override fun cancel() {
        internalScope.cancel()
    }
}