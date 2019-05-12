package com.github.mrmitew.bankapp.features.users.repository.internal

import com.github.mrmitew.bankapp.features.users.dto.toModel
import com.github.mrmitew.bankapp.features.users.repository.AuthService
import com.github.mrmitew.bankapp.features.users.repository.BackendApi
import com.github.mrmitew.bankapp.features.users.repository.RemoteUserRepository
import com.github.mrmitew.bankapp.features.users.vo.User

/**
 * All calls to our (fake) backend are done here. The [backend] can be an Retrofit instance instead.
 */
class RemoteUserRepositoryImpl(private val backend: BackendApi) : RemoteUserRepository,
    AuthService {
    override suspend fun getUserToken(appToken: String): String = backend.fetchUserToken(appToken)
    override suspend fun getPerson(token: String): User = backend.fetchPerson(token).toModel()
}