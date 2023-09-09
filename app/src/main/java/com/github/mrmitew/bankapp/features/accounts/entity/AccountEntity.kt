package com.github.mrmitew.bankapp.features.accounts.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.users.entity.UserEntity
import java.math.BigDecimal

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
    val currency: String,
    val balance: BigDecimal
) {
    companion object {
        internal const val TABLE_NAME = "account"
    }
}

fun AccountEntity.toDomainModel() = Account(
    id,
    name,
    iban,
    type,
    currency,
    balance
)

fun Account.toEntity(withUserId: Int) = AccountEntity(
    userId = withUserId,
    id = id,
    name = name,
    iban = iban,
    type = type,
    currency = currency,
    balance = balance
)