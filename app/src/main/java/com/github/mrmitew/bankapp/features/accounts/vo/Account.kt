package com.github.mrmitew.bankapp.features.accounts.vo

import com.github.mrmitew.bankapp.features.common.converter.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class Account(
    val id: Int,
    val name: String,
    val iban: String,
    val type: String,
    val currency: String,
    @Serializable(with = BigDecimalSerializer::class)
    val balance: BigDecimal
): java.io.Serializable {
    companion object {
        // We could use an enum or a sealed class as well, but just keeping things simple.
        const val TYPE_PAYMENT = "payment"
        const val TYPE_SAVINGS = "savings"
    }
}