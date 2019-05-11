package com.github.mrmitew.bankapp.features.users.repository.internal

import com.github.mrmitew.bankapp.features.transactions.dto.TransactionDTO
import com.github.mrmitew.bankapp.features.users.dto.UserDTO
import com.github.mrmitew.bankapp.features.users.repository.BackendApi
import java.math.BigDecimal
import java.util.*

/**
 * This is our backend. It is running on a remote machine. *wink*
 */
class FakeBackendImpl : BackendApi {
    override fun getUserToken(username: String): String = "HELLOWORLD"
    override fun getPerson(token: String): UserDTO {
        return when (token) {
            "HELLOWORLD" -> UserDTO(
                0,
                "Stefan",
                "Mitev",
                Calendar.getInstance().time
            )
            else -> throw IllegalArgumentException("Invalid token")
        }
    }

    override fun getTransactions(userAccessToken: String, accountId: Int): List<TransactionDTO> {
        // Backend logic....

        return when (accountId) {
            1 -> listOf(
                TransactionDTO(
                    1,
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
                    2,
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
                    3,
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
                    4,
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
                    5,
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
            2 ->
                listOf(
                    TransactionDTO(
                        1,
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
                        2,
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
            else -> throw IllegalArgumentException()
        }
    }

    override suspend fun addTransaction(userAccessToken: String, transaction: TransactionDTO) {
        // no-op
    }
}