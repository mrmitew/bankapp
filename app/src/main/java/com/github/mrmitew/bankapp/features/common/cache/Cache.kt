package com.github.mrmitew.bankapp.features.common.cache

/**
 * Created by Stefan Mitev on 16-4-19.
 */

/**
 * Representation of a cache.
 * A cache can be memory, disk or a network source.
 */
interface Cache<Key : Any, Value : Any> {
    suspend fun get(key: Key): Value?
    suspend fun set(key: Key, value: Value)
    suspend fun evict(key: Key)
    suspend fun evictAll()
}

