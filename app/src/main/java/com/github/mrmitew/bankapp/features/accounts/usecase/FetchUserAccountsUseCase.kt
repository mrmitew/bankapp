package com.github.mrmitew.bankapp.features.accounts.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.github.mrmitew.bankapp.features.accounts.repository.LocalAccountsRepository
import com.github.mrmitew.bankapp.features.accounts.repository.RemoteAccountsRepository
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.common.usecase.UseCase
import com.github.mrmitew.bankapp.features.common.vo.catchResult
import com.github.mrmitew.bankapp.features.common.vo.onFailure
import com.github.mrmitew.bankapp.features.users.repository.LocalUsersRepository

/**
 * Use case that will emit fresh bank account data from a remote repository (backend) and then
 * it will update the local repository and subscribe for local changes.
 * If the local data changes for any reason, it will emit them.
 *
 * Normally we can check if data is stale and then fetch from network but data like this, its better to
 * fetch from network first and then listen to local data store.
 *
 * Note that the class that can be unit tested as it has no Android related classes.
 * LiveData can still be unit tested under the JVM. See the test folder for an example.
 */
class FetchUserAccountsUseCase(
    private val localUsersRepository: LocalUsersRepository,
    private val localAccountsRepository: LocalAccountsRepository,
    private val remoteAccountsRepository: RemoteAccountsRepository
) : UseCase<Unit, LiveData<List<Account>>> {
    override suspend fun invoke(param: Unit): LiveData<List<Account>> {
        val user = localUsersRepository.getLoggedInUser()!!

        return liveData {
            catchResult {
                // Fetch from network
                val accounts = remoteAccountsRepository.fetchAccounts(user)

                // Emit right away, so that whoever is interested will get fresh data
                emit(accounts)

                if (accounts.isNotEmpty()) {
                    // Store to disk
                    @Suppress("ForbiddenComment")
                    // TODO: We should make a diff and remove the accounts that have been deleted on the server
                    // This of course won't happen in our example since everything is deterministic.
                    localAccountsRepository.storeAccounts(user, accounts)
                } else {
                    localAccountsRepository.deleteAccounts(user)
                }
            }.onFailure {
                // Log
                it.printStackTrace()
            }

            // By this time we already updated the local database (unless the network call failed)
            // We can subscribe to the local database and emit from there from now on
            // When database changes from an another use case (or for any reason), this use case
            // would always emit the latest data.
            emitSource(localAccountsRepository.getAccountsRefreshing(user))
        }
    }
}
