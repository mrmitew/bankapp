package com.github.mrmitew.bankapp.features.accounts.dto

import com.github.mrmitew.bankapp.features.accounts.vo.Account
import java.math.BigDecimal

/**
 * Data-Transfer-Object that represents the object we'll have to use when
 * working with our (fake) backend
 */
data class AccountDTO(
    val id: Int,
    val name: String,
    val iban: String,
    val type: String,
    val currency: String,
    val balance: BigDecimal
)

/**
 * Mapper for our domain specific model
 */
fun AccountDTO.toDomainModel() = Account(
    id,
    name,
    iban,
    type,
    currency,
    balance
)
