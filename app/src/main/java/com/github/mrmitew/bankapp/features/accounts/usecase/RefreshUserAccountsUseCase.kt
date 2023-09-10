package com.github.mrmitew.bankapp.features.accounts.usecase

import com.github.mrmitew.bankapp.features.accounts.repository.LocalAccountsRepository
import com.github.mrmitew.bankapp.features.accounts.repository.RemoteAccountsRepository
import com.github.mrmitew.bankapp.features.common.usecase.UseCase
import com.github.mrmitew.bankapp.features.common.vo.Result
import com.github.mrmitew.bankapp.features.common.vo.catchResult
import com.github.mrmitew.bankapp.features.common.vo.onFailure
import com.github.mrmitew.bankapp.features.users.repository.LocalUsersRepository

/**
 * Use case that will update the local repository with data from the remote.
 * If the use case does not error, then the operation has been successful.
 */
class RefreshUserAccountsUseCase(
    private val localUsersRepository: LocalUsersRepository,
    private val localAccountsRepository: LocalAccountsRepository,
    private val remoteAccountsRepository: RemoteAccountsRepository
) : UseCase<Unit, Result<Unit>> {
    override suspend fun invoke(param: Unit): Result<Unit> =
        catchResult {
            val user = localUsersRepository.getLoggedInUser()!!
            // Fetch from network
            val accounts = remoteAccountsRepository.fetchAccounts(user)
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
}
