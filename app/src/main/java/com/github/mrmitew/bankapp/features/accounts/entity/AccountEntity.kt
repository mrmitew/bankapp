package com.github.mrmitew.bankapp.features.accounts.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.github.mrmitew.bankapp.features.users.entity.UserEntity

/**
 * Database entity that represents a bank account that belongs to a [UserEntity]
 */
@Entity(
    tableName = AccountEntity.TABLE_NAME,
    indices = [Index("userId"), Index("id")],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("userId")
        )]
)
data class AccountEntity(
    val userId: Int,
    @PrimaryKey
    val id: Int,
    val name: String,
    val iban: String,
    val type: String,
    val currency: String
) {
    companion object {
        internal const val TABLE_NAME = "account"
    }
}