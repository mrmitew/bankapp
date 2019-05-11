package com.github.mrmitew.bankapp.features.users.repository.internal

import com.github.mrmitew.bankapp.features.common.usecase.UseCase
import com.github.mrmitew.bankapp.features.common.usecase.reuseInflight
import com.github.mrmitew.bankapp.features.users.repository.AuthService
import com.github.mrmitew.bankapp.features.users.repository.RemoteUserRepository

class AuthServiceImpl(remoteUserRepository: RemoteUserRepository) :
    AuthService {

    private class GetOrFetchUserTokenUseCase(private val remoteUserRepository: RemoteUserRepository) :
        UseCase<String, String> {
        private val internalUseCase = object : UseCase<String, String> {
            override suspend fun invoke(param: String): String = remoteUserRepository.getUserToken(param)
        }.reuseInflight()

        override suspend fun invoke(param: String): String = internalUseCase(param)!!
    }

    private val getOrFetchUserToken = GetOrFetchUserTokenUseCase(remoteUserRepository)

    override suspend fun getUserToken(appToken: String): String {
        return getOrFetchUserToken(appToken)
    }
}