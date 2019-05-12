package com.github.mrmitew.bankapp.common.usecase

import com.github.mrmitew.bankapp.features.common.usecase.UseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Created by Stefan Mitev on 1-4-19.
 */
class UseCaseTest {
    @Test
    fun `should return the expected value`() {
        val testUseCase = object :
            UseCase<Unit, Int> {
            override suspend operator fun invoke(param: Unit): Int {
                return 42
            }
        }

        runBlocking {
            // When use case is invoked
            val result = testUseCase(Unit)

            // Assert
            assertEquals(42, result)
        }
    }
}