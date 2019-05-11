package com.github.mrmitew.bankapp.features.common.usecase

/**
 * Created by Stefan Mitev on 4-5-19.
 */

interface UseCase<I : Any?, O : Any> {
    suspend operator fun invoke(param: I): O
}

suspend operator fun <O : Any> UseCase<Unit, O>.invoke() = invoke(Unit)