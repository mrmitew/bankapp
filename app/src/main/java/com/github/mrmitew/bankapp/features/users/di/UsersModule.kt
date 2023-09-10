package com.github.mrmitew.bankapp.features.users.di

import com.github.mrmitew.bankapp.features.storage.database.AppDatabase
import com.github.mrmitew.bankapp.features.users.repository.LocalUsersRepository
import com.github.mrmitew.bankapp.features.users.repository.RemoteUserRepository
import com.github.mrmitew.bankapp.features.users.repository.internal.LocalUsersRepositoryImpl
import com.github.mrmitew.bankapp.features.users.repository.internal.RemoteUserRepositoryImpl
import org.koin.dsl.module

/**
 * Created by Stefan Mitev on 12-5-19.
 */

val usersModule = module {
    single { get<AppDatabase>().userDao() }
    single { RemoteUserRepositoryImpl(get()) as RemoteUserRepository }
    single { LocalUsersRepositoryImpl(get()) as LocalUsersRepository }
}
