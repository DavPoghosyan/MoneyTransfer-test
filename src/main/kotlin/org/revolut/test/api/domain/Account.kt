package org.revolut.test.api.domain

import java.math.BigDecimal

data class AccountDTO(val account: Account? = null)

data class Account(val accountNumber: String,
                   val firstName: String,
                   val lastName: String,
                   val phoneNumber: String,
                   val balance: BigDecimal = BigDecimal.ZERO,
                   val isActive: Boolean = true)