@file:Suppress("TooManyFunctions")

package com.github.mrmitew.bankapp.features.common.vo

/**
 * Created by Stefan Mitev on 11-5-19.
 */

/**
 * A little "re-creation" of [kotlin.Result] just because we cannot
 * use it as a return type. The authors did that to prevent breaking
 * other people's apps when/if they change their internal APIs.
 *
 * We can also opt-in with:
 *
 * kotlinOptions {
 *  freeCompilerArgs = ["-Xallow-result-return-type"]
 * }
 */

sealed class Result<T> {
    class Success<T>(val value: T) : Result<T>()
    class Failure<T>(val throwable: Throwable) : Result<T>()

    val isSuccess: Boolean get() = this is Success<T>
    val isFailure: Boolean get() = this is Failure
}

fun <T> Result<T>.getOrNull(): T? = when (this) {
    is Result.Failure -> null
    is Result.Success -> value
}

fun <T> Result<T>.exceptionOrNull(): Throwable? =
    when (this) {
        is Result.Failure -> throwable
        is Result.Success -> null
    }

fun <T> Result<T>.getOrThrow(): T =
    when (this) {
        is Result.Failure -> throw throwable
        is Result.Success -> this.value
    }

fun <T> Result<T>.getOrElse(onFailure: (exception: Throwable) -> T): T {
    return when (this) {
        is Result.Success -> this.value
        is Result.Failure -> onFailure(throwable)
    }
}

fun <T> Result<T>.getOrDefault(defaultValue: T): T {
    return when (this) {
        is Result.Success -> this.value
        is Result.Failure -> defaultValue
    }
}

fun <R, T> Result<T>.map(transform: (value: T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(
            transform(value)
        )
        is Result.Failure -> Result.Failure(
            throwable
        )
    }
}

fun <T> Result<T>.mapFailure(transform: (exception: Throwable) -> Throwable): Result<T> {
    return when (this) {
        is Result.Success -> this
        is Result.Failure -> Result.Failure(
            transform(throwable)
        )
    }
}

inline fun <T, R> Result<T>.fold(
    onSuccess: (T) -> R,
    onFailure: (Throwable) -> R
): R {
    return when (this) {
        is Result.Success -> onSuccess(value)
        is Result.Failure -> onFailure(throwable)
    }
}

inline fun <T> Result<T>.onSuccess(action: (T) -> Unit): Result<T> {
    if (this is Result.Success) action(value)
    return this
}

inline fun <T> Result<T>.onFailure(action: (Throwable) -> Unit): Result<T> {
    if (this is Result.Failure) action(this.throwable)
    return this
}

fun <T> Result<T>.throwOnFailure() {
    if (this is Result.Failure) throw throwable
}

@Suppress("TooGenericExceptionCaught")
inline fun <R> catchResult(block: () -> R): Result<R> {
    return try {
        Result.Success(block())
    } catch (e: Throwable) {
        Result.Failure(e)
    }
}

inline fun <T> Result<T>.recover(transform: (exception: Throwable) -> T): Result<T> {
    return when (val exception = exceptionOrNull()) {
        null -> this
        else -> Result.Success(transform(exception))
    }
}

inline fun <T> Result<T>.recoverCatching(transform: (exception: Throwable) -> T): Result<T> {
    return when (val exception = exceptionOrNull()) {
        null -> this
        else -> catchResult { transform(exception) }
    }
}

fun <R> R.toSuccess(): Result<R> {
    return Result.Success(this)
}

fun <I : Throwable, O : Any> I.toFailure(): Result<O> {
    return Result.Failure(this)
}

fun success() = Result.Success(Unit)
