package com.github.mrmitew.bankapp

import android.content.Context
import com.github.mrmitew.bankapp.features.accounts.di.accountsModule
import com.github.mrmitew.bankapp.features.auth.di.authModule
import com.github.mrmitew.bankapp.features.common.di.commonModule
import com.github.mrmitew.bankapp.features.storage.di.storageModule
import com.github.mrmitew.bankapp.features.transactions.di.transactionsModule
import com.github.mrmitew.bankapp.features.users.di.usersModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

// TODO: Split each feature in a separate gradle module

object DependencyInjection {
    fun with(applicationContext: Context) {
        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(
                accountsModule,
                transactionsModule,
                usersModule,
                storageModule,
                authModule,
                commonModule
            )
        }
    }
}