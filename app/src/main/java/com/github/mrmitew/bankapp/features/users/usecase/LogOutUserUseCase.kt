package com.github.mrmitew.bankapp.features.users.usecase

import com.github.mrmitew.bankapp.features.common.usecase.UseCase
import com.github.mrmitew.bankapp.features.users.repository.LocalUsersRepository

/**
 * Use case that knows what to do when user has to be logged out.
 * Currently unused, because there is no real implementation.
 * We just exit the app instead. Its just for demo purposes.
 */
class LogOutUserUseCase(private val localUsersRepository: LocalUsersRepository) :
    UseCase<Unit, Result<Unit>> {

    override suspend fun invoke(param: Unit) =
        runCatching { localUsersRepository.deleteUserSettings() }
}
