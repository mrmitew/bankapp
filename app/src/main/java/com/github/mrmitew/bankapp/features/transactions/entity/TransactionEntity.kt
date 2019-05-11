package com.github.mrmitew.bankapp.features.transactions.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.github.mrmitew.bankapp.features.accounts.entity.AccountEntity
import com.github.mrmitew.bankapp.features.transactions.converter.BigDecimalConverter
import com.github.mrmitew.bankapp.features.transactions.vo.Transaction
import java.math.BigDecimal

/**
 * Database entity that represents a transaction which belongs to a bank [AccountEntity].
 */
@Entity(
    tableName = TransactionEntity.TABLE_NAME,
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("accountId")
        )]
)
@TypeConverters(BigDecimalConverter::class)
data class TransactionEntity(
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

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}

fun TransactionEntity.toDomainModel() = Transaction(
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
    name,
    description,
    comment,
    date,
    mutationType,
    amount,
    targetName,
    targetAccount
)