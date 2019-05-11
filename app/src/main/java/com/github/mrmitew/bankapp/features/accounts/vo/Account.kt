package com.github.mrmitew.bankapp.features.accounts.vo

import androidx.recyclerview.widget.DiffUtil
import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: Int,
    val name: String,
    val iban: String,
    val type: String,
    val currency: String
): java.io.Serializable {
    companion object {
        // We could use an enum or a sealed class as well, but just keeping things simple.
        const val TYPE_PAYMENT = "payment"
        const val TYPE_SAVINGS = "savings"

        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Account>() {
            override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean =
                oldItem.id == newItem.id
        }
    }
}