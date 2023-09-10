package com.github.mrmitew.bankapp.features.auth.internal

import com.github.mrmitew.bankapp.features.auth.vo.Token
import com.github.mrmitew.bankapp.features.common.usecase.ReuseInflightUseCase
import com.github.mrmitew.bankapp.features.common.usecase.UseCase
import com.github.mrmitew.bankapp.features.common.usecase.reuseInflight

/**
 * Use case whose purpose is to get or fetch user token, based on token's availability
 * and its status.
 *
 * Since we favor composition over inheritance, this use case wraps an [ReuseInflightUseCase],
 * that will prevent multiple callers to trigger fetching a token while we wait to get it from the network.
 * Callers that call this use case while another caller is waiting on a token, will hook to the pending request
 * and will suspend until token is retrieved.
 *
 * Internally, the inflight-enabled use case will get/fetch a token from a [TokenCacheFacade].
 * That class hides the implementation details whether the token comes from a cache or network.
 */
internal class GetOrFetchUserTokenUseCase(private val tokenCacheFacade: TokenCacheFacade) :
    UseCase<String, Token> {
    private val internalUseCase = object : UseCase<String, Token> {
        override suspend fun invoke(param: String): Token = tokenCacheFacade.cache.get(param)!!
    }.reuseInflight()

    override suspend fun invoke(param: String): Token = internalUseCase(param)!!
}
