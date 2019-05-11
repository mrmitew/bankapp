package com.github.mrmitew.bankapp.features.users.usecase

import com.github.mrmitew.bankapp.features.common.usecase.UseCase
import com.github.mrmitew.bankapp.features.users.vo.User
import com.github.mrmitew.bankapp.features.users.repository.AuthService
import com.github.mrmitew.bankapp.features.users.repository.LocalUsersRepository
import com.github.mrmitew.bankapp.features.users.repository.RemoteUserRepository

typealias Username = String

class LogInUserUseCase(
    private val localUsersRepository: LocalUsersRepository,
    private val remoteUserRepository: RemoteUserRepository,
    private val authService: AuthService
) :
    UseCase<String, Result<User>> {

    override suspend fun invoke(param: Username) =
        runCatching {
            val token = authService.getToken(param)
            val user = remoteUserRepository.getPerson(token)
            localUsersRepository.createUser(user)
            localUsersRepository.login(user)
            user
        }
}