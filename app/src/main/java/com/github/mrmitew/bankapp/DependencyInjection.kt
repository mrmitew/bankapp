package com.github.mrmitew.bankapp

import android.content.Context
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.github.mrmitew.bankapp.features.accounts.repository.internal.FakeRemoteAccountsRepository
import com.github.mrmitew.bankapp.features.accounts.repository.internal.LocalAccountsRepository
import com.github.mrmitew.bankapp.features.accounts.ui.AccountListViewModel
import com.github.mrmitew.bankapp.features.accounts.usecase.RefreshUserAccountsUseCase
import com.github.mrmitew.bankapp.features.common.database.AppDatabase
import com.github.mrmitew.bankapp.features.login.ui.LoginViewModel
import com.github.mrmitew.bankapp.features.users.repository.AuthService
import com.github.mrmitew.bankapp.features.users.repository.BackendApi
import com.github.mrmitew.bankapp.features.users.repository.LocalUsersRepository
import com.github.mrmitew.bankapp.features.users.repository.RemoteUserRepository
import com.github.mrmitew.bankapp.features.users.repository.internal.AuthServiceImpl
import com.github.mrmitew.bankapp.features.users.repository.internal.FakeBackendImpl
import com.github.mrmitew.bankapp.features.users.repository.internal.FakeRemoteUserRepository
import com.github.mrmitew.bankapp.features.users.repository.internal.LocalUsersRepositoryImpl
import com.github.mrmitew.bankapp.features.users.usecase.LogInUserUseCase
import com.github.mrmitew.bankapp.features.users.usecase.LogOutUserUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

// FIXME: User input should either be propagated to the open helper factory, or we should
// try opening the database with the user pin before we construct the factory.
// Now it is hardcoded, just to make things a little simpler.
// We should never hardcode secrets in the app, even if they are cleverly obfuscated
// (like using DexGuard to encrypt parts of the secret, constructing the secret from multiple places,
// utilizing reflection, JNI calls etc) If static analysis fails, there is always room for
// dynamic analysis.
// Don't judge for the hardcoded pin, please! :)
val USER_PIN = charArrayOf('0', '0', '0', '0')

// TODO: Split into multiple modules for each feature
private val appModule = module {
    // Accounts
    viewModel { AccountListViewModel(get()) }
    single {
        RefreshUserAccountsUseCase(
            get(),
            get<LocalAccountsRepository>(),
            get<FakeRemoteAccountsRepository>()
        )
    }
    single { FakeRemoteAccountsRepository() }
    single { LocalAccountsRepository(get()) }
    single { get<AppDatabase>().accountDao() }

    // Users
    viewModel { LoginViewModel(get()) }
    single { LogInUserUseCase(get(), get(), get()) }
    single { LogOutUserUseCase(get()) }
    single { get<AppDatabase>().userDao() }
    single { FakeRemoteUserRepository(get()) as RemoteUserRepository }
    single { LocalUsersRepositoryImpl(get()) as LocalUsersRepository }
    single { FakeBackendImpl() as BackendApi }

    // Common

    // Storage
    single { AppDatabase.getInstance(androidContext(), get()) }
//    single { (userPin: CharArray) -> SafeHelperFactory(userPin) }
    single { SafeHelperFactory(USER_PIN) }

    // Auth
    single { AuthServiceImpl(get()) as AuthService }
}

object DependencyInjection {
    fun init(applicationContext: Context) {
        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(appModule)
        }
    }
}