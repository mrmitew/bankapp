package com.github.mrmitew.bankapp.features.transactions.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.github.mrmitew.bankapp.features.accounts.entity.AccountEntity
import com.github.mrmitew.bankapp.features.transactions.vo.Transaction
import java.math.BigDecimal

/**
 * Database entity that represents a transaction which belongs to a bank [AccountEntity].
 */
@Entity(
    tableName = TransactionEntity.TABLE_NAME,
    indices = [Index("accountId"), Index("transactionId")],
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"]
        )
    ]
)
data class TransactionEntity(
    @PrimaryKey
    val transactionId: String,
    val accountId: Int,
    val name: String,
    val description: String?,
    val comment: String?,
    val date: String,
    val mutationType: String,
    val amount: BigDecimal,
    val targetName: String,
    val targetAccount: String
) {
    companion object {
        // We cannot use "transaction" as a table name since its a special keyword
        internal const val TABLE_NAME = "transaction_entity"
    }
}

/**
 * Mappers for the other layers of the app
 */

fun TransactionEntity.toDomainModel() = Transaction(
    transactionId,
    accountId,
    name,
    description,
    comment,
    date,
    mutationType,
    amount,
    targetName,
    targetAccount
)

fun Transaction.toEntity() = TransactionEntity(
    id,
    accountId,
    name,
    description,
    comment,
    date,
    mutationType,
    amount,
    targetName,
    targetAccount
)