package com.github.mrmitew.bankapp.features.accounts

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mrmitew.bankapp.features.accounts.repository.AccountsRepository
import com.github.mrmitew.bankapp.features.accounts.repository.internal.RemoteAccountsRepositoryImpl
import com.github.mrmitew.bankapp.features.accounts.usecase.RefreshUserAccountsUseCase
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.users.entity.UserEntity
import com.github.mrmitew.bankapp.features.users.repository.UserDao
import com.github.mrmitew.bankapp.features.users.repository.internal.LocalUsersRepositoryImpl
import com.github.mrmitew.bankapp.features.users.vo.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

/**
 * Created by Stefan Mitev on 4-5-19.
 */

@ExperimentalCoroutinesApi
class RefreshUserAccountsUseCaseTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `get from local repo and refresh it with a fetch from a remote repo`() = testScope.runBlockingTest {
        val getUserProjectsUseCase = RefreshUserAccountsUseCase(
            LocalUsersRepositoryImpl(object : UserDao {
                override suspend fun createUser(user: UserEntity) {
                }

                override suspend fun deleteUser(id: Int) {
                }
            }).apply {
                login(User(0, "Stefan", "Mitev"))
            },
            TestLocalAccountsRepository(),
            RemoteAccountsRepositoryImpl()
        )
        val livedata = getUserProjectsUseCase(Unit)
        assertEquals(null, livedata.value)
        advanceTimeBy(5000)
        assertTrue(livedata.value?.isNotEmpty() == true)
    }
}

private class TestLocalAccountsRepository : AccountsRepository {
    private val accountsStream = MutableLiveData<List<Account>>()

    override suspend fun getAccountsRefreshing(user: User): LiveData<List<Account>> = accountsStream

    override suspend fun storeAccounts(user: User, accounts: List<Account>) {
        accountsStream.value = accounts
    }

    override suspend fun deleteAccounts(user: User) {
        throw UnsupportedOperationException()
    }

    override suspend fun getAccountBalanceRefreshing(accountId: Int): LiveData<BigDecimal> {
        throw UnsupportedOperationException()
    }
}