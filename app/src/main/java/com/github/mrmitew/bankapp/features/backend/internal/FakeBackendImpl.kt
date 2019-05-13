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

    // FIXME: Account balance doesn't really match the transactions.. let's say we don't keep old transactions :)
    private val accounts = mutableListOf(
        AccountDTO(1, "Stefan Mitev", "NLXXGIMB123IBAN", Account.TYPE_PAYMENT, "EUR", BigDecimal(1_992)),
        AccountDTO(2, "Stefan Mitev", "NLXXJFAKE123IBAN", Account.TYPE_PAYMENT, "EUR", BigDecimal(6000)),
        AccountDTO(3, "Stefan Mitev", "NLXXAMS123IBAN", Account.TYPE_SAVINGS, "EUR", BigDecimal(9_090))
    )

    private val transactionsAccount1 = mutableListOf(
        TransactionDTO(
            "2805c74c-cabf-41b3-af3d-96504098b165",
            1,
            "Samantha",
            "Passvolgnr: 001 10-05-2019 19:00",
            "For the bowling that one time",
            "10 May 2019",
            "Internetbankieren",
            BigDecimal(20).setScale(2, BigDecimal.ROUND_HALF_DOWN),
            "Stefan Mitev",
            "NLXXJUMBO123IBAN"
        ),
        TransactionDTO(
            "6d97a4fe-4dc5-4226-a57e-005f5d466dad",
            1,
            "Bowling place",
            "Passvolgnr: 001 10-05-2019 19:00",
            null,
            "9 May 2019",
            "Betaalautomat",
            BigDecimal(-20).setScale(2, BigDecimal.ROUND_HALF_DOWN),
            "Bowling place",
            "NLXXAMS12334IBAN"
        ),
        TransactionDTO(
            "3b858c24-0acb-4355-ab7b-a7f5ff42b50d",
            1,
            "John Smith",
            "Passvolgnr: 001 10-05-2019 19:00",
            null,
            "8 May 2019",
            "Internetbankieren",
            BigDecimal(100).setScale(2, BigDecimal.ROUND_HALF_DOWN),
            "Stefan Mitev",
            "NLXXAMS123IBAN"
        ),
        TransactionDTO(
            "bc92ed9f-2b67-4660-8d17-615939b00b16",
            1,
            "Jumbo Utrecht",
            "Passvolgnr: 001 10-05-2019 19:00",
            null,
            "8 May 2019",
            "Betaalautomaat",
            BigDecimal(-23.72).setScale(2, BigDecimal.ROUND_HALF_DOWN),
            "Jumbo Utrecht",
            "NLXXJUMBO126343IBAN"
        ),
        TransactionDTO(
            "9e50ffbf-65d2-43e7-b1eb-bda8ad79095a",
            1,
            "Albert Heijn Amsterdam",
            "Passvolgnr: 001 10-05-2019 19:00",
            null,
            "8 May 2019",
            "Betaalautomaat",
            BigDecimal(-4.12).setScale(2, BigDecimal.ROUND_HALF_DOWN),
            "Albert Heijn Amsterdam",
            "NLXXAMS125543IBAN"
        ),
        TransactionDTO(
            "871f4a12-b875-4cf6-8d64-9f2633b1548f",
            1,
            "Albert Heijn Amsterdam",
            "Passvolgnr: 001 10-05-2019 19:00",
            null,
            "7 May 2019",
            "Betaalautomaat",
            BigDecimal(-6.72).setScale(2, BigDecimal.ROUND_HALF_DOWN),
            "Albert Heijn Amsterdam",
            "NLXXAMS12323IBAN"
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
            "NLXXJUMBO1213IBAN"
        ),
        TransactionDTO(
            "4c619838-df62-4c9d-8523-e779c2f65a29",
            2,
            "Albert Heijn Amsterdam",
            "Passvolgnr: 001 10-05-2019 19:00",
            null,
            "11 May 2019",
            "Betaalautomaat",
            BigDecimal(-10.22).setScale(2, BigDecimal.ROUND_HALF_DOWN),
            "Albert Heijn Amsterdam",
            "NLXXAMS1236IBAN"
        )
    )

    private val transactionsAccount3 = mutableListOf(
        TransactionDTO(
            "150536ed-d170-4e7f-b526-17bfda5b1e53",
            3,
            "Amsterdam Schiphol",
            "Passvolgnr: 001 10-05-2019 10:00",
            null,
            "12 May 2019",
            "Betaalautomaat",
            BigDecimal(-20.15).setScale(2, BigDecimal.ROUND_HALF_DOWN),
            "Jumbo Utrecht",
            "NLXXJUMBO12345IBAN"
        ),
        TransactionDTO(
            "44538a9f-4818-496b-b66d-f9f49648b84f",
            3,
            "Kinepolis Utrecht",
            "Passvolgnr: 001 10-05-2019 20:00",
            null,
            "12 May 2019",
            "Betaalautomaat",
            BigDecimal(-20.00).setScale(2, BigDecimal.ROUND_HALF_DOWN),
            "Kinepolis Utrecht",
            "NLXXAMS1243IBAN"
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
            3 -> transactionsAccount3
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
            3 -> transactionsAccount3.add(0, transaction).also {
                accounts[2] = accounts[2].copy(balance = accounts[2].balance.add(transaction.amount))
            }
            else -> throw IllegalArgumentException()
        }
    }

    override suspend fun updateAccountBalance(userAccessToken: String, accountId: Int, newBalance: BigDecimal) {
        when (accountId) {
            1 -> accounts[0] = accounts[0].copy(balance = newBalance)
            2 -> accounts[1] = accounts[1].copy(balance = newBalance)
            3 -> accounts[2] = accounts[2].copy(balance = newBalance)
            else -> throw IllegalArgumentException()
        }
    }

    override suspend fun fetchAccountBalance(userAccessToken: String, accountId: Int): BigDecimal {
        return when (accountId) {
            1 -> accounts[0].balance
            2 -> accounts[1].balance
            3 -> accounts[2].balance
            else -> throw IllegalArgumentException()
        }
    }
}