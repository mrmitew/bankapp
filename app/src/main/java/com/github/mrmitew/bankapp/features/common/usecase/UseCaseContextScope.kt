package com.github.mrmitew.bankapp.features.common.usecase

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * Created by Stefan Mitev on 11-5-19.
 */

/**
 * Used internally to create an internal coroutine scope for a given use case
 */
internal class UseCaseContextScope(context: CoroutineContext) : CoroutineScope {
    override val coroutineContext: CoroutineContext = context
}