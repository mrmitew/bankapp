package com.github.mrmitew.bankapp.features.transactions.ui

import com.github.mrmitew.bankapp.features.accounts.repository.LocalAccountsRepository
import com.github.mrmitew.bankapp.features.accounts.repository.RemoteAccountsRepository
import com.github.mrmitew.bankapp.features.common.usecase.UseCase
import com.github.mrmitew.bankapp.features.common.vo.Result
import com.github.mrmitew.bankapp.features.common.vo.catchResult
import com.github.mrmitew.bankapp.features.common.vo.getOrThrow
import com.github.mrmitew.bankapp.features.common.vo.onSuccess
import com.github.mrmitew.bankapp.features.transactions.repository.LocalTransactionsRepository
import com.github.mrmitew.bankapp.features.transactions.repository.RemoteTransactionsRepository
import com.github.mrmitew.bankapp.features.transactions.vo.Transaction
import java.util.Calendar
import java.util.UUID

/**
 * Use case that will perform a transaction between two accounts.
 * Normally, we would request only one and the backend will do the other one,
 * but since this is a demo app, don't judge please :)
 */
class AddTransactionUseCase(
    // Injecting the strings, instead of a context, so that we can unit test it.
    private val depositString: String,
    private val withdrawString: String,
    private val localTransactionsRepository: LocalTransactionsRepository,
    private val remoteTransactionsRepository: RemoteTransactionsRepository,
    private val localAccountsRepository: LocalAccountsRepository,
    private val remoteAccountsRepository: RemoteAccountsRepository
) :
    UseCase<AddTransactionDTO, Result<Unit>> {

    override suspend fun invoke(param: AddTransactionDTO) =
        catchResult {
            val mutationType1 = if (param.isDeposit) depositString else withdrawString
            val mutationType2 = if (!param.isDeposit) depositString else withdrawString

            val date =
                Calendar.getInstance().time.toString() // We don't format it to not overcomplicate things

            val transaction1 = Transaction(
                id = UUID.randomUUID().toString(),
                accountId = param.sourceAccount.id,
                name = param.sourceAccount.name,
                comment = param.comment,
                description = mutationType1,
                date = date,
                mutationType = mutationType1,
                amount = if (param.isDeposit) param.amount else param.amount.negate(),
                targetName = param.targetAccount.name,
                targetAccount = param.targetAccount.iban
            )

            val transaction2 = Transaction(
                id = UUID.randomUUID().toString(),
                accountId = param.targetAccount.id,
                name = param.targetAccount.name,
                comment = param.comment,
                description = mutationType2,
                date = date,
                mutationType = mutationType2,
                amount = if (!param.isDeposit) param.amount else param.amount.negate(),
                targetName = param.sourceAccount.name,
                targetAccount = param.sourceAccount.iban
            )

            // Let's pretend they are done in an actual transaction :)
            catchResult {
                // Update the "backend"
                remoteTransactionsRepository.addTransaction(transaction1)
                remoteTransactionsRepository.addTransaction(transaction2)
            }.onSuccess {
                // Let's pretend they are done in an actual transaction :)
                catchResult {
                    // Update the local database
                    localTransactionsRepository.addTransaction(transaction1)
                    localTransactionsRepository.addTransaction(transaction2)
                }.getOrThrow()
            }.getOrThrow()


            // Update local data source with what our backend tells us

            localAccountsRepository.updateAccountBalance(
                param.sourceAccount.id,
                remoteAccountsRepository.fetchAccountBalance(param.sourceAccount.id)
            )

            localAccountsRepository.updateAccountBalance(
                param.targetAccount.id,
                remoteAccountsRepository.fetchAccountBalance(param.targetAccount.id)
            )

            Unit
        }
}