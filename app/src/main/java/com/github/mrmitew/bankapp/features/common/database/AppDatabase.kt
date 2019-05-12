package com.github.mrmitew.bankapp.features.common.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.github.mrmitew.bankapp.features.accounts.entity.AccountEntity
import com.github.mrmitew.bankapp.features.accounts.repository.AccountDao
import com.github.mrmitew.bankapp.features.common.database.AppDatabase.Companion.DATABASE_VERSION
import com.github.mrmitew.bankapp.features.transactions.converter.BigDecimalConverter
import com.github.mrmitew.bankapp.features.transactions.entity.TransactionEntity
import com.github.mrmitew.bankapp.features.transactions.repository.internal.TransactionDao
import com.github.mrmitew.bankapp.features.users.entity.UserEntity
import com.github.mrmitew.bankapp.features.users.repository.UserDao

/**
 * Created by Stefan Mitev on 4-5-19.
 */

@Database(
    entities = [
        UserEntity::class,
        AccountEntity::class,
        TransactionEntity::class
    ],
    version = DATABASE_VERSION,
    exportSchema = false
)
@TypeConverters(BigDecimalConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun accountDao(): AccountDao
    abstract fun transactionDao(): TransactionDao

    companion object {
        internal const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "appdatabase.db"

        private val MIGRATION_OLD_NEW = object : Migration(DATABASE_VERSION - 1, DATABASE_VERSION) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, safeHelperFactory: SafeHelperFactory): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context, safeHelperFactory).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context, safeHelperFactory: SafeHelperFactory) =
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .openHelperFactory(safeHelperFactory)
                .addMigrations(MIGRATION_OLD_NEW)
                .build()
    }
}