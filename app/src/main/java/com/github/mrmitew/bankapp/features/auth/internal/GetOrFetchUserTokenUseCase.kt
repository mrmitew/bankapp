package com.github.mrmitew.bankapp.features.auth.internal

import com.github.mrmitew.bankapp.features.auth.internal.TokenCacheFacade
import com.github.mrmitew.bankapp.features.auth.vo.Token
import com.github.mrmitew.bankapp.features.common.usecase.UseCase
import com.github.mrmitew.bankapp.features.common.usecase.reuseInflight

internal class GetOrFetchUserTokenUseCase(private val tokenCacheFacade: TokenCacheFacade) :
    UseCase<String, Token> {
    private val internalUseCase = object : UseCase<String, Token> {
        override suspend fun invoke(param: String): Token = tokenCacheFacade.cache.get(param)!!
    }.reuseInflight()

    override suspend fun invoke(param: String): Token = internalUseCase(param)!!
}