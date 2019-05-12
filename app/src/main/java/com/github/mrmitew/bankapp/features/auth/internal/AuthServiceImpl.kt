package com.github.mrmitew.bankapp.features.auth.internal

import com.github.mrmitew.bankapp.features.auth.AuthService
import com.github.mrmitew.bankapp.features.auth.vo.Token
import com.github.mrmitew.bankapp.features.users.repository.RemoteUserRepository

class AuthServiceImpl(remoteUserRepository: RemoteUserRepository) :
    AuthService {

    private val getOrFetchUserToken =
        GetOrFetchUserTokenUseCase(
            TokenCacheFacade(
                remoteUserRepository
            )
        )

    override suspend fun getUserToken(appToken: String): Token {
        return getOrFetchUserToken(appToken)
    }
}