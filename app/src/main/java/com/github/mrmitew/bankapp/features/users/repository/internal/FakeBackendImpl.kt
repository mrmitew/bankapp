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
            2 ->
                listOf(
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
            else -> throw IllegalArgumentException()
        }
    }

    override suspend fun addTransaction(userAccessToken: String, transaction: TransactionDTO) {
        // no-op
    }
}