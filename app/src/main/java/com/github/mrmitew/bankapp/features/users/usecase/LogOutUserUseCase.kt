package com.github.mrmitew.bankapp.features.users.usecase

import com.github.mrmitew.bankapp.features.common.usecase.UseCase
import com.github.mrmitew.bankapp.features.users.repository.LocalUsersRepository

class LogOutUserUseCase(private val localUsersRepository: LocalUsersRepository) :
    UseCase<Unit, Result<Unit>> {

    override suspend fun invoke(param: Unit) =
        runCatching { localUsersRepository.deleteUserSettings() }
}