package com.github.mrmitew.bankapp.features.users.repository.internal

import com.github.mrmitew.bankapp.features.users.repository.AuthService
import com.github.mrmitew.bankapp.features.users.repository.RemoteUserRepository

class AuthServiceImpl(remoteUserRepository: RemoteUserRepository) :
    AuthService {

    private val getOrFetchUserToken =
        GetOrFetchUserTokenUseCase(TokenCacheFacade(remoteUserRepository))

    override suspend fun getUserToken(appToken: String): String {
        return getOrFetchUserToken(appToken)
    }
}