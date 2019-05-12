package com.github.mrmitew.bankapp.features.users.repository

import com.github.mrmitew.bankapp.features.auth.vo.Token
import com.github.mrmitew.bankapp.features.users.vo.User

/**
 * Remote data source, which expose domain specific classes, not DTOs.
 * This way we are not going to be bound to backend specific classes so that if any changes occur
 * for any reason (ex. migration to another backend), we would only need to map the new DTOs to our
 * domain models.
 */
interface RemoteUserRepository {
    suspend fun getUserToken(appToken: String): Token
    suspend fun getPerson(token: String): User
}