package com.github.mrmitew.bankapp.features.auth.di

import com.github.mrmitew.bankapp.features.auth.AuthService
import com.github.mrmitew.bankapp.features.auth.internal.AuthServiceImpl
import org.koin.dsl.module
import java.util.UUID

/**
 * Created by Stefan Mitev on 12-5-19.
 */

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

val authModule = module {
    single { AuthServiceImpl(get()) as AuthService }
}
