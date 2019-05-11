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
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Account>() {
            override fun areItemsTheSame(oldItem: Account, newItem: Account): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Account, newItem: Account): Boolean =
                oldItem.id == newItem.id
        }
    }
}