package org.revolut.test.api.web.controllers

import io.javalin.Context
import org.revolut.test.api.domain.AccountDTO
import org.revolut.test.api.domain.TransferDTO
import org.revolut.test.api.domain.service.AccountService
import org.revolut.test.api.domain.service.TransferService
import java.math.BigDecimal

class TransferController(private val transferService: TransferService) {

    fun transfer(ctx: Context) {
        ctx.validatedBody<TransferDTO>()
                .check(({ it.transfer!!.amount.signum() > 0}))
                .getOrThrow().transfer?.also { transfer ->
            transferService.execute(transfer)
        }

    }
}