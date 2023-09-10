package com.github.mrmitew.bankapp.features.transactions.ui

import com.github.mrmitew.bankapp.features.accounts.vo.Account
import java.math.BigDecimal

data class AddTransactionDTO(
    val sourceAccount: Account,
    val targetAccount: Account,
    val isDeposit: Boolean,
    val comment: String?,
    val amount: BigDecimal
)
