package org.revolut.test.api.domain

import org.joda.time.DateTime
import java.math.BigDecimal

data class TransferDTO(val transfer: Transfer? = null)

data class Transfer(val sender: String,
                    val receiver: String,
                    val amount: BigDecimal = BigDecimal.ZERO,
                    var message: String = "",
                    @Transient
                    val proceedAt: DateTime = DateTime.now(),
                    @Transient
                    var isExecuted: Boolean = false) {
}