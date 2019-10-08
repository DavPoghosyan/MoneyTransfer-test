package org.revolut.test.api.domain.repository

import org.revolut.test.api.domain.Transfer
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.math.BigDecimal
import javax.sql.DataSource

private object Transfers : Table() {
    val sender: Column<String> = varchar("SENDER", 30)
    val receiver: Column<String> = varchar("RECEIVER", 30)
    val proceedAt: Column<DateTime> = datetime("PROCEED_AT")
    val amount: Column<BigDecimal> = decimal("AMOUNT", 19,  4)
    val isExecuted: Column<Boolean> = bool("IS_EXECUTED")
    val message: Column<String> = varchar("MESSAGE", 70)

    fun toDomain(row: ResultRow): Transfer {
        return Transfer(
                sender = row[sender],
                receiver = row[receiver],
                proceedAt = row[proceedAt],
                amount = row[amount],
                isExecuted = row[isExecuted],
                message = row[message]
        )
    }
}


class TransferRepository(private val dataSource: DataSource) {

    init {
        transaction(Database.connect(dataSource)) {
            SchemaUtils.create(Transfers)
        }
    }

    fun save(transfer: Transfer) {
        transaction(Database.connect(dataSource)) {
            Transfers.insert { row ->
                row[sender] = transfer.sender
                row[receiver] = transfer.receiver
                row[amount] = transfer.amount
                row[isExecuted] = transfer.isExecuted
                row[message] = transfer.message
                row[proceedAt] = transfer.proceedAt

            }
        }
    }

}