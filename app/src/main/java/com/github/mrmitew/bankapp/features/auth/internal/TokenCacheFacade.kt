package com.github.mrmitew.bankapp.features.auth.internal

import com.github.mrmitew.bankapp.features.auth.vo.Token
import com.github.mrmitew.bankapp.features.storage.cache.Cache
import com.github.mrmitew.bankapp.features.storage.cache.Fetcher
import com.github.mrmitew.bankapp.features.storage.cache.plus
import com.github.mrmitew.bankapp.features.users.repository.RemoteUserRepository

/**
 * A [Cache] facade that hides the implementation of how a [Token] is being retrieved.
 * If it is available in the cache and it isn't expired, we'll use that one. If, however,
 * a token doesn't exist or it already expired, we'll fetch a new one, utilizing the [RemoteUserRepository].
 */
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
        return (System.currentTimeMillis() > token.expireDate)
    }

    /**
     * Composing the memory cache and the network fetcher (which is also sort of a cache)
     * and exposing a single [Cache] as a public API to work with.
     */
    val cache = memoryCache + networkFetcher
}