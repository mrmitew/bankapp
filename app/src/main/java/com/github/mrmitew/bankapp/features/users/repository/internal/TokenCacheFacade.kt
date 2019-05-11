package com.github.mrmitew.bankapp.features.users.repository.internal

import com.github.mrmitew.bankapp.features.common.cache.Cache
import com.github.mrmitew.bankapp.features.common.cache.Fetcher
import com.github.mrmitew.bankapp.features.common.cache.plus
import com.github.mrmitew.bankapp.features.users.repository.RemoteUserRepository

internal class TokenCacheFacade(private val remoteUserRepository: RemoteUserRepository) {
    // TODO: Token should be some sort of a POJO that would contain expiration date,
    // refresh token etc.

    // TODO: To implement using [android.util.LruCache]
    private val memoryCache = object : Cache<String, String> {
        private val internalMap = HashMap<String, String>()

        override suspend fun get(key: String): String? {
            if (isTokenExpired(key)) {
                // TODO: return null, and let the network fetcher deal with it
            }
            return internalMap[key]
        }

        override suspend fun set(key: String, value: String) {
            internalMap[key] = value
        }

        override suspend fun evict(key: String) {
            internalMap.remove(key)
        }

        override suspend fun evictAll() {
            internalMap.clear()
        }
    }

    private val networkFetcher = object : Fetcher<String, String> {
        override suspend fun get(key: String): String? {
            if (isTokenExpired(key)) {
                // TODO: Refresh it
            }
            return remoteUserRepository.getUserToken(key)
        }
    }

    // TODO: To implement some logic here
    private fun isTokenExpired(token: String) = false

    val cache = memoryCache + networkFetcher
}