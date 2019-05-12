package com.github.mrmitew.bankapp.features.transactions.ui

import com.github.mrmitew.bankapp.features.accounts.repository.LocalAccountsRepository
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.common.usecase.UseCase
import com.github.mrmitew.bankapp.features.users.repository.LocalUsersRepository

class GetAvailableAccountsForTransactionUseCase(
    private val usersRepository: LocalUsersRepository,
    private val localAccountsRepository: LocalAccountsRepository
) :
    UseCase<Account, List<Account>> {
    override suspend fun invoke(param: Account) =
        localAccountsRepository.getAccounts(usersRepository.getLoggedInUser()!!)
            .filter { it.id != param.id }
}