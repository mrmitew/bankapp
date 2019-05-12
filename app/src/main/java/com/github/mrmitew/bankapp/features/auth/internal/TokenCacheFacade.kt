package com.github.mrmitew.bankapp.features.auth.internal

import com.github.mrmitew.bankapp.features.auth.vo.Token
import com.github.mrmitew.bankapp.features.common.cache.Cache
import com.github.mrmitew.bankapp.features.common.cache.Fetcher
import com.github.mrmitew.bankapp.features.common.cache.plus
import com.github.mrmitew.bankapp.features.users.repository.RemoteUserRepository

internal class TokenCacheFacade(private val remoteUserRepository: RemoteUserRepository) {
    // TODO: To implement using [android.util.LruCache]
    private val memoryCache = object : Cache<String, Token> {
        private val internalMap = HashMap<String, Token>()

        override suspend fun get(key: String): Token? {
            if (isTokenExpired(internalMap[key])) {
                println("[AUTH] Token has expired")
                return null
            }
            return internalMap[key]
        }

        override suspend fun set(key: String, value: Token) {
            internalMap[key] = value
        }

        override suspend fun evict(key: String) {
            internalMap.remove(key)
        }

        override suspend fun evictAll() {
            internalMap.clear()
        }
    }

    private val networkFetcher = object : Fetcher<String, Token> {
        override suspend fun get(key: String): Token? {
            println("[AUTH] Fetching a new token")
            return remoteUserRepository.getUserToken(key)
        }
    }

    private fun isTokenExpired(token: Token?): Boolean {
        if (token == null) return true
        return (System.currentTimeMillis() < token.expireDate)
    }

    val cache = memoryCache + networkFetcher
}