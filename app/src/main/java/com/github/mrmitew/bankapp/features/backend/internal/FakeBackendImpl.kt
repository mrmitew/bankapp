package com.github.mrmitew.bankapp.features.backend.internal

import com.github.mrmitew.bankapp.features.accounts.dto.AccountDTO
import com.github.mrmitew.bankapp.features.accounts.vo.Account
import com.github.mrmitew.bankapp.features.auth.vo.Token
import com.github.mrmitew.bankapp.features.transactions.dto.TransactionDTO
import com.github.mrmitew.bankapp.features.users.dto.UserDTO
import com.github.mrmitew.bankapp.features.backend.BackendApi
import com.github.mrmitew.bankapp.features.users.vo.User
import java.math.BigDecimal
import java.util.*

/**
 * This is our backend. It is running on a remote machine. *wink*
 */
class FakeBackendImpl : BackendApi {
    private var currentUserToken: Token? = null

    private val accounts = mutableListOf(
        AccountDTO(1, "Stefan Mitev", "NLXXJUMBO123IBAN", Account.TYPE_PAYMENT, "EUR", BigDecimal(1_992)),
        AccountDTO(2, "Stefan Mitev", "NLXXAMS123IBAN", Account.TYPE_SAVINGS, "EUR", BigDecimal(9_090))
    )

    private val transactionsAccount1 = mutableListOf(
        TransactionDTO(
            "2805c74c-cabf-41b3-af3d-96504098b165",
            1,
            "Jumbo Utrecht",
            "Passvolgnr: 001 10-05-2019 19:00",
            null,
            "11 May 2019",
            "Betaalautomaat",
            BigDecimal(-23.72).setScale(2, BigDecimal.ROUND_HALF_DOWN),
            "Jumbo Utrecht",
            "NLXXJUMBO123IBAN"
        ),
        TransactionDTO(
            "6d97a4fe-4dc5-4226-a57e-005f5d466dad",
            1,
            "Albert Heijn Amsterdam",
            "Passvolgnr: 001 10-05-2019 19:00",
            null,
            "11 May 2019",
            "Betaalautomaat",
            BigDecimal(-23.72).setScale(2, BigDecimal.ROUND_HALF_DOWN),
            "Albert Heijn Amsterdam",
            "NLXXAMS123IBAN"
        ),
        TransactionDTO(
            "bc92ed9f-2b67-4660-8d17-615939b00b16",
            1,
            "Jumbo Utrecht",
            "Passvolgnr: 001 10-05-2019 19:00",
            null,
            "11 May 2019",
            "Betaalautomaat",
            BigDecimal(-23.72).setScale(2, BigDecimal.ROUND_HALF_DOWN),
            "Jumbo Utrecht",
            "NLXXJUMBO123IBAN"
        ),
        TransactionDTO(
            "9e50ffbf-65d2-43e7-b1eb-bda8ad79095a",
            1,
            "Albert Heijn Amsterdam",
            "Passvolgnr: 001 10-05-2019 19:00",
            null,
            "11 May 2019",
            "Betaalautomaat",
            BigDecimal(-23.72).setScale(2, BigDecimal.ROUND_HALF_DOWN),
            "Albert Heijn Amsterdam",
            "NLXXAMS123IBAN"
        ),
        TransactionDTO(
            "871f4a12-b875-4cf6-8d64-9f2633b1548f",
            1,
            "Albert Heijn Amsterdam",
            "Passvolgnr: 001 10-05-2019 19:00",
            null,
            "11 May 2019",
            "Betaalautomaat",
            BigDecimal(-23.72).setScale(2, BigDecimal.ROUND_HALF_DOWN),
            "Albert Heijn Amsterdam",
            "NLXXAMS123IBAN"
        )
    )

    private val transactionsAccount2 = mutableListOf(
        TransactionDTO(
            "71aec513-49a5-45d9-aa68-81de7f18cc93",
            2,
            "Jumbo Utrecht",
            "Passvolgnr: 001 10-05-2019 19:00",
            null,
            "11 May 2019",
            "Betaalautomaat",
            BigDecimal(-23.72).setScale(2, BigDecimal.ROUND_HALF_DOWN),
            "Jumbo Utrecht",
            "NLXXJUMBO123IBAN"
        ),
        TransactionDTO(
            "4c619838-df62-4c9d-8523-e779c2f65a29",
            2,
            "Albert Heijn Amsterdam",
            "Passvolgnr: 001 10-05-2019 19:00",
            null,
            "11 May 2019",
            "Betaalautomaat",
            BigDecimal(-23.72).setScale(2, BigDecimal.ROUND_HALF_DOWN),
            "Albert Heijn Amsterdam",
            "NLXXAMS123IBAN"
        )
    )

    override suspend fun fetchUserToken(username: String): Token {
        val token = Token(
            accessToken = UUID.randomUUID().toString(),
            expireDate = System.currentTimeMillis() + 1000 * 10
        )
        currentUserToken = token
        return token
    }

    override suspend fun fetchPerson(token: String): UserDTO {
        return when (token) {
            currentUserToken?.accessToken -> UserDTO(
                0,
                "Stefan",
                "Mitev",
                Calendar.getInstance().time
            )
            else -> throw IllegalArgumentException("Invalid token")
        }
    }

    override suspend fun fetchAccounts(userAccessToken: String, user: User): List<AccountDTO> {
        return accounts
    }

    override suspend fun fetchTransactions(userAccessToken: String, accountId: Int): List<TransactionDTO> {
        // Backend logic....
        return when (accountId) {
            1 -> transactionsAccount1
            2 -> transactionsAccount2
            else -> throw IllegalArgumentException()
        }
    }

    override suspend fun addTransaction(userAccessToken: String, transaction: TransactionDTO) {
        // if amount is negative, it will be subtracted
        // also, add on top, because it will be easier instead of sorting it
        when (transaction.accountId) {
            1 -> transactionsAccount1.add(0, transaction).also {
                accounts[0] = accounts[0].copy(balance = accounts[0].balance.add(transaction.amount))
            }
            2 -> transactionsAccount2.add(0, transaction).also {
                accounts[1] = accounts[1].copy(balance = accounts[1].balance.add(transaction.amount))
            }
            else -> throw IllegalArgumentException()
        }
    }

    override suspend fun updateAccountBalance(userAccessToken: String, accountId: Int, newBalance: BigDecimal) {
        when (accountId) {
            1 -> accounts[0] = accounts[0].copy(balance = newBalance)
            2 -> accounts[1] = accounts[1].copy(balance = newBalance)
            else -> throw IllegalArgumentException()
        }
    }

    override suspend fun fetchAccountBalance(userAccessToken: String, accountId: Int): BigDecimal {
        return when (accountId) {
            1 -> accounts[0].balance
            2 -> accounts[1].balance
            else -> throw IllegalArgumentException()
        }
    }
}