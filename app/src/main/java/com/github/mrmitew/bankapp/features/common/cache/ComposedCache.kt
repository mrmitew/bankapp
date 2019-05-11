package com.github.mrmitew.bankapp.features.common.cache

import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

/**
 * Created by Stefan Mitev on 16-4-19.
 */

/**
 * Cache, composed by one or more other cache sources.
 */
interface ComposedCache<Key : Any, Value : Any> : Cache<Key, Value> {
    val parents: List<Cache<*, *>>
}

/**
 * Way to compose one cache with another cache using the + operator
 */
operator fun <Key : Any, Value : Any> Cache<Key, Value>.plus(b: Cache<Key, Value>) = compose(b)

/**
 * Iterates over parents to determine if there are any circular references
 */
fun <Key : Any, Value : Any> ComposedCache<Key, Value>.hasLoop(): Boolean {
    val baseCaches = mutableListOf<Cache<*, *>>()

    val cacheQueue = mutableListOf<Cache<*, *>>()
    cacheQueue.addAll(parents)

    while (cacheQueue.isNotEmpty()) {
        val cache = cacheQueue.removeAt(0)

        when (cache) {
            is ComposedCache -> cacheQueue.addAll(cache.parents)
            else -> baseCaches.add(cache)
        }
    }

    return baseCaches.size != baseCaches.distinct().size
}

/**
 * Composing one cache with another by chaining caches together
 */
fun <Key : Any, Value : Any> Cache<Key, Value>.compose(b: Cache<Key, Value>): Cache<Key, Value> {
    return object : ComposedCache<Key, Value> {
        init {
            require(!hasLoop()) { "Cache creates a circular reference" }
        }

        override val parents: List<Cache<*, *>>
            get() = listOf(this@compose, b)

        override suspend fun evict(key: Key) {
            listOf(this@compose, b).forEach {
                supervisorScope {
                    launch { it.evict(key) }
                }
            }
        }

        override suspend fun get(key: Key): Value? {
            return this@compose.get(key) ?: b.get(key)?.apply {
                this@compose.set(key, this)
            }
        }

        override suspend fun set(key: Key, value: Value) {
            listOf(this@compose, b).forEach {
                supervisorScope {
                    launch { it.set(key, value) }
                }
            }
        }

        override suspend fun evictAll() {
            parents.forEach {
                supervisorScope {
                    launch { it.evictAll() }
                }
            }
        }
    }
}