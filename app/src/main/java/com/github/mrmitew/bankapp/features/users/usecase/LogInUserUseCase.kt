package com.github.mrmitew.bankapp.features.users.usecase

import com.github.mrmitew.bankapp.APP_TOKEN
import com.github.mrmitew.bankapp.USER_PIN
import com.github.mrmitew.bankapp.features.common.usecase.UseCase
import com.github.mrmitew.bankapp.features.common.vo.catchResult
import com.github.mrmitew.bankapp.features.common.vo.Result
import com.github.mrmitew.bankapp.features.common.vo.onFailure
import com.github.mrmitew.bankapp.features.auth.AuthService
import com.github.mrmitew.bankapp.features.users.repository.LocalUsersRepository
import com.github.mrmitew.bankapp.features.users.repository.RemoteUserRepository
import com.github.mrmitew.bankapp.features.users.vo.User

typealias PinCode = CharArray

class LogInUserUseCase(
    private val localUsersRepository: LocalUsersRepository,
    private val remoteUserRepository: RemoteUserRepository,
    private val authService: AuthService
) :
    UseCase<CharArray, Result<User>> {

    override suspend fun invoke(param: PinCode) =
        catchResult {
            if (!tryOpenEncryptedDatabaseWith(param)) {
                throw WrongPasswordException()
            }
            val token = authService.getUserToken(APP_TOKEN)
            val user = remoteUserRepository.getPerson(token.accessToken)
            localUsersRepository.createUser(user)
            localUsersRepository.login(user)
            user
        }.onFailure {
            it.printStackTrace()
        }

    private fun tryOpenEncryptedDatabaseWith(param: PinCode): Boolean {
        // TODO: Instead of pin comparison, we should try opening the database with the pin
        // and not having the pin hardcoded in the app :)
        return param.contentEquals(USER_PIN)
    }
}

class WrongPasswordException : RuntimeException()