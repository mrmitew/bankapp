package com.github.mrmitew.bankapp.features.common.usecase

import com.github.mrmitew.bankapp.features.common.vo.catchResult
import com.github.mrmitew.bankapp.features.common.vo.getOrThrow
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * Created by Stefan Mitev on 16-4-19.
 */

/**
 * A [UseCase] decorator that will execute a decorated [UseCase] asynchronously and
 * it will cache the deferred result until execution completes.
 *
 * This will prevent multiple callers to trigger the same business logic,
 * while the result is being computed. They, instead, will hook in-flight for the pending operation and
 * will suspend until result is ready.
 *
 * If a caller executes the use case after a completion of a previous operation, then the use case
 * will be executed once again.
 *
 * Great for prevention of fetching multiple user tokens if multiple network requests are fired
 * when a user token has been invalidated.
 */
class ReuseInflightUseCase<I : Any, O : Any>(private val useCase: UseCase<I, O>) :
    UseCase<I, O> {
    init {
        check(useCase !is ReuseInflightUseCase<*, *>) { "Do not directly chain reuseInflight" }
    }

    private val cache = mutableMapOf<I, Deferred<O?>>()

    override suspend operator fun invoke(param: I): O? {
        val cached = cache[param]
        if (cached != null) {
            return cached.await()
        } else {
            val deferred = coroutineScope {
                async { useCase.invoke(param) }
            }

            // Store the result to be re-used by others
            cache[param] = deferred

            // TODO: Use internal, smaller scope + job that can be cancelled
            // We don't use supervisor/coroutine-Scope on purpose. They'll suspend the current
            // execution of invoke(), while we want that to happen in parallel.
            GlobalScope.launch {
                // Remove the item from the cache once the deferred completes
                cache.remove(param)?.await()
            }

            // Wait until job is done
            val output = catchResult { deferred.await() }
            return output.getOrThrow()
        }
    }
}

fun <I : Any, O : Any> UseCase<I, O>.reuseInflight(): UseCase<I, O> =
    ReuseInflightUseCase(this)