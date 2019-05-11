package com.github.mrmitew.bankapp.features.users.repository.internal

import com.github.mrmitew.bankapp.features.users.vo.User
import com.github.mrmitew.bankapp.features.users.dto.toModel
import com.github.mrmitew.bankapp.features.users.repository.AuthService
import com.github.mrmitew.bankapp.features.users.repository.BackendApi
import com.github.mrmitew.bankapp.features.users.repository.RemoteUserRepository

/**
 * All calls to our fake backend are done here. The [backend] can be an Retrofit instance instead.
 */
class FakeRemoteUserRepository(private val backend: BackendApi) : RemoteUserRepository,
    AuthService {
    override suspend fun getToken(username: String): String = backend.getToken(username)
    override suspend fun getPerson(token: String): User = backend.getPerson(token).toModel()
}