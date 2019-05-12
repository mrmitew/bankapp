package com.github.mrmitew.bankapp

import android.content.Context
import com.commonsware.cwac.saferoom.SafeHelperFactory
import com.github.mrmitew.bankapp.features.accounts.repository.LocalAccountsRepository
import com.github.mrmitew.bankapp.features.accounts.repository.RemoteAccountsRepository
import com.github.mrmitew.bankapp.features.accounts.repository.internal.FakeRemoteAccountsRepository
import com.github.mrmitew.bankapp.features.accounts.repository.internal.RoomAccountsRepository
import com.github.mrmitew.bankapp.features.accounts.ui.AccountListViewModel
import com.github.mrmitew.bankapp.features.accounts.usecase.FetchUserAccountsUseCase
import com.github.mrmitew.bankapp.features.accounts.usecase.RefreshUserAccountsUseCase
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.common.database.AppDatabase
import com.github.mrmitew.bankapp.features.login.ui.LoginViewModel
import com.github.mrmitew.bankapp.features.transactions.repository.LocalTransactionsRepository
import com.github.mrmitew.bankapp.features.transactions.repository.RemoteTransactionsRepository
import com.github.mrmitew.bankapp.features.transactions.repository.internal.FakeRemoteTransactionsRepository
import com.github.mrmitew.bankapp.features.transactions.repository.internal.RoomTransactionsRepository
import com.github.mrmitew.bankapp.features.transactions.ui.GetAccountBalanceUseCase
import com.github.mrmitew.bankapp.features.transactions.ui.RefreshAccountTransactionsUseCase
import com.github.mrmitew.bankapp.features.transactions.ui.TransactionsViewModel
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
import java.util.*

// FIXME: User input should either be propagated to the open helper factory, or we should
// try opening the database with the user pin before we construct the factory.
// Now it is hardcoded, just to make things a little simpler.
// We should never hardcode secrets in the app, even if they are cleverly obfuscated
// (like using DexGuard to encrypt parts of the secret, constructing the secret from multiple places,
// utilizing reflection, JNI calls etc) If static analysis fails, there is always room for
// dynamic analysis.
// Don't judge for the hardcoded pin, please! :)
val USER_PIN = charArrayOf('0', '0', '0', '0')
// Some app secret that we shouldn't normally keep in the app and definitely not hard coded in the code :)
val APP_TOKEN = UUID.randomUUID().toString()

// TODO: Split each feature in a separate gradle module

private val accountsModule = module {
    viewModel { AccountListViewModel(get(), get()) }
    single { RefreshUserAccountsUseCase(get(), get(), get()) }
    single { FetchUserAccountsUseCase(get(), get(), get()) }
    single { FakeRemoteAccountsRepository() as RemoteAccountsRepository }
    single { RoomAccountsRepository(get()) as LocalAccountsRepository }
    single { get<AppDatabase>().accountDao() }
}

private val transactionsModule = module {
    viewModel { (account: Account) -> TransactionsViewModel(get(), get(), account) } // Assisted injection
    single { RoomTransactionsRepository(get()) as LocalTransactionsRepository }
    single { FakeRemoteTransactionsRepository(get(), get()) as RemoteTransactionsRepository }
    single { get<AppDatabase>().transactionDao() }
    single { RefreshAccountTransactionsUseCase(get(), get()) }
}

private val usersModule = module {
    viewModel { LoginViewModel(get()) }
    single { LogInUserUseCase(get(), get(), get()) }
    single { LogOutUserUseCase(get()) }
    single { get<AppDatabase>().userDao() }
    single { FakeRemoteUserRepository(get()) as RemoteUserRepository }
    single { LocalUsersRepositoryImpl(get()) as LocalUsersRepository }
    single { FakeBackendImpl() as BackendApi }
}

private val storageModule = module {
    single { AppDatabase.getInstance(androidContext(), get()) }
    // TODO: Assisted injection. We copy it here, because the array will be cleared out.
    //  If user goes back to login it wouldn't work second time around.
    single { SafeHelperFactory(USER_PIN.copyOf()) }
}

private val authModule = module {
    single { AuthServiceImpl(get()) as AuthService }
}

private val commonModule = module {
    // Common
    single { GetAccountBalanceUseCase(get(), get()) }
}

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