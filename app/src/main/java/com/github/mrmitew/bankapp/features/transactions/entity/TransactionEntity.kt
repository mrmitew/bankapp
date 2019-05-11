package com.github.mrmitew.bankapp.features.transactions.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.github.mrmitew.bankapp.features.accounts.entity.AccountEntity
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
        internal const val TABLE_NAME = "transaction"
    }

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}