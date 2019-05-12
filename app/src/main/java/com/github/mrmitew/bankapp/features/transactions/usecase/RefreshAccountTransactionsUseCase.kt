package com.github.mrmitew.bankapp.features.transactions.usecase

import androidx.lifecycle.LiveData
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.common.usecase.Cancellable
import com.github.mrmitew.bankapp.features.common.usecase.UseCase
import com.github.mrmitew.bankapp.features.common.usecase.UseCaseContextScope
import com.github.mrmitew.bankapp.features.common.vo.catchResult
import com.github.mrmitew.bankapp.features.common.vo.onFailure
import com.github.mrmitew.bankapp.features.transactions.repository.LocalTransactionsRepository
import com.github.mrmitew.bankapp.features.transactions.repository.RemoteTransactionsRepository
import com.github.mrmitew.bankapp.features.transactions.vo.Transaction
import kotlinx.coroutines.*

/**
 * A Use Case that will fetch transactions from local database and emit them
 * immediately.
 * In the meantime it will fetch transactions from the network and add them to the database.
 * Since we are already observing the database, we'll be able to emit the new transactions as well.
 *
 * This Use Case works in sort-of reverse order compared to
 * [com.github.mrmitew.bankapp.features.accounts.usecase.FetchUserAccountsUseCase] just so it can
 * demonstrate another way of doing get/fetch/refresh. For a banking app, we would always be
 * fetching data from network and we'll even not have a database.
 */
class RefreshAccountTransactionsUseCase(
    private val localTransactionsRepository: LocalTransactionsRepository,
    private val remoteTransactionsRepository: RemoteTransactionsRepository
) :
    UseCase<Account, LiveData<List<Transaction>>>, Cancellable {
    private val internalScope
        get() = // Normally, it should be .IO since we'll be doing disk operations,
            // but I haven't made mechanism to mock it in tests
            UseCaseContextScope(SupervisorJob() + Dispatchers.Main)

    override suspend fun invoke(param: Account): LiveData<List<Transaction>> {
        internalScope.launch {
            catchResult {
                val transactions = remoteTransactionsRepository.getTransactions(param.id)
                if (transactions.isNotEmpty()) {
                    localTransactionsRepository.addTransactions(transactions)
                } else {
                    localTransactionsRepository.deleteTransactions(param.id)
                }
            }.onFailure {
                // Log
                it.printStackTrace()
            }
        }
        return localTransactionsRepository.getTransactions(accountId = param.id)
    }

    override fun cancel() {
        internalScope.cancel()
    }
}