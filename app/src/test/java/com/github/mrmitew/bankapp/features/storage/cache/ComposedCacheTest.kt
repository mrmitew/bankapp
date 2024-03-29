package com.github.mrmitew.bankapp.features.storage.cache

import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

/**
 * Created by Stefan Mitev on 16-4-19.
 */

class ComposedCacheTest {
    companion object {
        private const val TEST_KEY = "key"
    }

    @Test
    fun `should return value from second cache`() {
        val emptyMemoryCache = object : Cache<String, Int> {
            override suspend fun get(key: String): Int? = null
            override suspend fun set(key: String, value: Int) {}
            override suspend fun evict(key: String) {}
            override suspend fun evictAll() {}
        }

        val diskCache = object : Cache<String, Int> {
            override suspend fun get(key: String): Int? = 42
            override suspend fun set(key: String, value: Int) {}
            override suspend fun evict(key: String) {}
            override suspend fun evictAll() {}
        }

        val cache = emptyMemoryCache.compose(diskCache)

        runBlocking {
            assertThat(cache.get(TEST_KEY)).isEqualTo(42)
        }
    }

    @Test
    fun `should return value from last cache`() {
        val emptyMemoryCache = object : Cache<String, Int> {
            override suspend fun get(key: String): Int? = null
            override suspend fun set(key: String, value: Int) {}
            override suspend fun evict(key: String) {}
            override suspend fun evictAll() {}
        }

        val diskCache = object : Cache<String, Int> {
            override suspend fun get(key: String): Int? = null
            override suspend fun set(key: String, value: Int) {}
            override suspend fun evict(key: String) {}
            override suspend fun evictAll() {}
        }

        val network = object : Fetcher<String, Int> {
            override suspend fun get(key: String): Int? = 42
        }

        val cache = emptyMemoryCache
            .compose(diskCache)
            .compose(network)

        runBlocking {
            assertThat(cache.get(TEST_KEY)).isEqualTo(42)
        }
    }

    @Test
    fun `should return value from first cache`() {
        val emptyMemoryCache = object : Cache<String, Int> {
            override suspend fun get(key: String): Int? = 24
            override suspend fun set(key: String, value: Int) {}
            override suspend fun evict(key: String) {}
            override suspend fun evictAll() {}
        }

        val diskCache = object : Cache<String, Int> {
            override suspend fun get(key: String): Int? = 42
            override suspend fun set(key: String, value: Int) {}
            override suspend fun evict(key: String) {}
            override suspend fun evictAll() {}
        }

        val cache = emptyMemoryCache.compose(diskCache)

        runBlocking {
            assertThat(cache.get(TEST_KEY)).isEqualTo(24)
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw exception upon circular dependency`() {
        // Given two caches
        val cache1 = mockk<Cache<String, Int>>()
        val cache2 = mockk<Cache<String, Int>>()

        // When one cache is composed with itself at some point
        cache1.compose(cache2).compose(cache1)

        // should throw an exception
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw exception when cache composes itself`() {
        // Given two caches
        val cache1 = mockk<Cache<String, Int>>()

        // When one cache is composed with itself at some point
        cache1.compose(cache1)

        // should throw an exception
    }
}