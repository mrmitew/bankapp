package com.github.mrmitew.bankapp.features.transactions.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.github.mrmitew.bankapp.features.accounts.repository.LocalAccountsRepository
import com.github.mrmitew.bankapp.features.accounts.repository.RemoteAccountsRepository
import com.github.mrmitew.bankapp.features.common.usecase.UseCase
import java.math.BigDecimal

/**
 * Use case that returns a stream of account balance, once taken from network
 * and then observing a local storage, so that in case it changes from an external source,
 * it will emit the latest value.
 */
class GetAccountBalanceUseCase(
    private val localAccountsRepository: LocalAccountsRepository,
    private val remoteAccountsRepository: RemoteAccountsRepository
) :
    UseCase<Int, LiveData<BigDecimal>> {
    override suspend fun invoke(param: Int) =
        liveData {
            // Emit account balance from the remote data source,
            // update the local store and subscribe to it
            val actualBalance = remoteAccountsRepository.fetchAccountBalance(param)
            emit(actualBalance)
            localAccountsRepository.updateAccountBalance(param, actualBalance)
            emitSource(localAccountsRepository.getAccountBalanceRefreshing(param))
        }
}