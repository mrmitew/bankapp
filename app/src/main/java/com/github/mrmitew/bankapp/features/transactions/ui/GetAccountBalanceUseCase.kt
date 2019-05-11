package com.github.mrmitew.bankapp.features.transactions.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.github.mrmitew.bankapp.features.accounts.repository.internal.FakeRemoteAccountsRepository
import com.github.mrmitew.bankapp.features.common.usecase.UseCase
import java.math.BigDecimal

class GetAccountBalanceUseCase(private val remoteAccountsRepository: FakeRemoteAccountsRepository) :
    UseCase<Int, LiveData<BigDecimal>> {
    override suspend fun invoke(param: Int) =
        liveData<BigDecimal> { remoteAccountsRepository.getAccountBalance(param) }
}