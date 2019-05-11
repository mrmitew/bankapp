package com.github.mrmitew.bankapp.features.users.repository

import com.github.mrmitew.bankapp.features.users.dto.UserDTO

/**
 * Since we are not going to communicate with an actual backend, we'll pretend as if there is one.
 * That is the public api /endpoints/ of our fake backend.
 * Notice that we expose DTOs here like [UserDTO].
 */
interface BackendApi {
    fun getUserToken(username: String): String
    fun getPerson(token: String): UserDTO
}