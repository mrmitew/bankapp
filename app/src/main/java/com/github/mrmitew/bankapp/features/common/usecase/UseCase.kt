package com.github.mrmitew.bankapp.features.common.usecase

/**
 * Created by Stefan Mitev on 4-5-19.
 */

/**
 * Use case, also known as Interactor in "Clean Architecture".
 * It is a very small business unit that can execute an arbitrary logic, given an optional parameter.
 *
 * Overriding the invoke operator allows us to call use cases like:
 * val getUserProfile = GetUserProfileUseCase()
 * val profile = getUserProfile()
 */
interface UseCase<I : Any?, O : Any> {
    suspend operator fun invoke(param: I): O?
}

suspend operator fun <O : Any> UseCase<Unit, O>.invoke() = invoke(Unit)