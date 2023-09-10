package com.github.mrmitew.bankapp.features.backend.di

import com.github.mrmitew.bankapp.features.backend.BackendApi
import com.github.mrmitew.bankapp.features.backend.internal.FakeBackendImpl
import org.koin.dsl.module

/**
 * Created by Stefan Mitev on 12-5-19.
 */

val backendModule = module {
    single { FakeBackendImpl() as BackendApi }
}
