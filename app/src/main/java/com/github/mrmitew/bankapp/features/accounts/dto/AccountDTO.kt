package com.github.mrmitew.bankapp.features.accounts.dto

import com.github.mrmitew.bankapp.features.accounts.vo.Account
import java.math.BigDecimal

data class AccountDTO(
    val id: Int,
    val name: String,
    val iban: String,
    val type: String,
    val currency: String,
    val balance: BigDecimal
)

fun AccountDTO.toDomainModel() = Account(
    id,
    name,
    iban,
    type,
    currency,
    balance
)