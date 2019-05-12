package com.github.mrmitew.bankapp.features.transactions.vo

import androidx.recyclerview.widget.DiffUtil
import com.github.mrmitew.bankapp.features.common.converter.BigDecimalSerializer
import com.github.mrmitew.bankapp.features.transactions.entity.TransactionEntity
import kotlinx.serialization.Serializable
import java.math.BigDecimal

/**
 * I don't use [@Parcelable] just because its android specific.
 * Serializable classes can be unit tested without additional mocking frameworks.
 *
 * Model that represents a single transaction.
 */
@Serializable
data class Transaction(
    val id: String,
    val accountId: Int,
    val name: String,
    val description: String?,
    val comment: String?,
    val date: String,
    val mutationType: String,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    val targetName: String,
    val targetAccount: String
) : java.io.Serializable {
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean =
                oldItem.id == newItem.id
        }
    }
}