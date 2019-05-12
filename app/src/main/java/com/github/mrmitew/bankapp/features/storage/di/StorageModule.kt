package com.github.mrmitew.bankapp.features.storage.di

import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.github.mrmitew.bankapp.features.auth.di.USER_PIN
import com.github.mrmitew.bankapp.features.storage.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Created by Stefan Mitev on 12-5-19.
 */

val storageModule = module {
    single { AppDatabase.getInstance(androidContext(), get()) }
    // TODO: Assisted injection.
    single { SafeHelperFactory(USER_PIN) }
}