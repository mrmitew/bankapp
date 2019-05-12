package com.github.mrmitew.bankapp.features.accounts.vo

import com.github.mrmitew.bankapp.features.common.converter.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

/**
 * The domain model of a bank [Account].
 * We use [Serializable] instead of [Parcelable], because we don't want to have an Android
 * dependency when we use this class in unit tests.
 */
@Serializable
data class Account(
    val id: Int,
    val name: String,
    val iban: String,
    // TODO: We can have a sealed class instead of a field. SavingsAccount and PaymentAccount
    val type: String,
    val currency: String,
    @Serializable(with = BigDecimalSerializer::class)
    val balance: BigDecimal
): java.io.Serializable {
    companion object {
        // TODO We can have a sealed class. SavingsAccount and PaymentAccount
        const val TYPE_PAYMENT = "payment"
        const val TYPE_SAVINGS = "savings"
    }
}