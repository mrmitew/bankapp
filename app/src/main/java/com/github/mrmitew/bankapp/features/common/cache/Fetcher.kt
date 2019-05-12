package com.github.mrmitew.bankapp.features.common.cache

/**
 * Fetcher is type of [Cache] which does not support evicting or setting
 * values.
 */
interface Fetcher<Key : Any, Value : Any> : Cache<Key, Value> {
    override suspend fun evict(key: Key) {
        // No-op
    }

    override suspend fun evictAll() {
        // No-op
    }

    override suspend fun set(key: Key, value: Value) {
        // No-op
    }
}