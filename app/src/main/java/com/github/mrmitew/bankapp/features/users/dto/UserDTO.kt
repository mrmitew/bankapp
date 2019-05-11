package com.github.mrmitew.bankapp.features.users.dto

import com.github.mrmitew.bankapp.features.users.vo.User
import java.util.*

/**
 * Created by Stefan Mitev on 11-5-19.
 */

data class UserDTO(val personId: Int = 0, val firstName: String, val lastName: String, val registrationDate: Date)

fun UserDTO.toModel() = User(
    id = personId,
    firstName = firstName,
    lastName = lastName
)