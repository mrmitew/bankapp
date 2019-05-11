package com.github.mrmitew.bankapp.features.users.repository.internal

import com.github.mrmitew.bankapp.features.users.dto.UserDTO
import com.github.mrmitew.bankapp.features.users.repository.BackendApi
import java.util.*

/**
 * This is our backend. It is running on a remote machine. *wink*
 */
class FakeBackendImpl : BackendApi {
    override fun getUserToken(username: String): String = "HELLOWORLD"
    override fun getPerson(token: String): UserDTO {
        return when (token) {
            "HELLOWORLD" -> UserDTO(
                0,
                "Stefan",
                "Mitev",
                Calendar.getInstance().time
            )
            else -> throw IllegalArgumentException("Invalid token")
        }
    }
}