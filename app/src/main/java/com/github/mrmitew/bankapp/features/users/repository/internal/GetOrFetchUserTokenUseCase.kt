package com.github.mrmitew.bankapp.features.users.repository.internal

import com.github.mrmitew.bankapp.features.common.usecase.UseCase
import com.github.mrmitew.bankapp.features.common.usecase.reuseInflight

internal class GetOrFetchUserTokenUseCase(private val tokenCacheFacade: TokenCacheFacade) :
    UseCase<String, String> {
    private val internalUseCase = object : UseCase<String, String> {
        override suspend fun invoke(param: String): String = tokenCacheFacade.cache.get(param)!!
    }.reuseInflight()

    override suspend fun invoke(param: String): String = internalUseCase(param)!!
}