package org.revolut.test.api.domain.repository

import io.javalin.NotFoundResponse
import org.revolut.test.api.domain.Account
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.Op
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import org.joda.time.DateTime
import java.math.BigDecimal
import java.math.BigInteger
import javax.sql.DataSource

private object Accounts : Table() {
    val accountNumber: Column<String> = varchar("ACCOUNT_NUMBER", 30).primaryKey()
    val firstName: Column<String> = varchar("FIRST_NAME", 30)
    val lastName: Column<String> = varchar("LAST_NAME", 30)
    val phoneNumber: Column<String> = varchar("PHONE_NUMBER", 20)
    val balance: Column<BigDecimal> = decimal("BALANCE", 19,  4)
    val isActive: Column<Boolean> = bool("IS_ACTIVE")

    fun toDomain(row: ResultRow): Account {
        return Account(
                accountNumber = row[accountNumber],
                firstName = row[firstName],
                lastName = row[lastName],
                phoneNumber = row[phoneNumber],
                balance = row[balance],
                isActive = row[isActive]
        )
    }
}


class AccountRepository(private val dataSource: DataSource) {

    private val accounts = listOf(
            Account(firstName = "Alice", lastName = "Dilyan", accountNumber = "TEST0001", phoneNumber = "37495085426", balance = BigDecimal.valueOf(115.2)),
            Account(firstName = "Bob", lastName = "Mirali", accountNumber = "TEST0002", phoneNumber = "48521215", balance = BigDecimal.valueOf(5000)),
            Account(firstName = "Li", lastName = "Chang", accountNumber = "TEST0003", phoneNumber = "1111115242", balance = BigDecimal.valueOf(2500))
    )

    init {
        transaction(Database.connect(dataSource)) {
            SchemaUtils.create(Accounts)
        }
        transaction(Database.connect(dataSource)) {
            Accounts.batchInsert(accounts) { account ->
                this[Accounts.accountNumber] = account.accountNumber
                this[Accounts.phoneNumber] = account.phoneNumber
                this[Accounts.firstName] = account.firstName
                this[Accounts.lastName] = account.lastName
                this[Accounts.balance] = account.balance
                this[Accounts.isActive] = account.isActive

            }
        }
    }

    fun findByAccountNumber(accountNumber: String): Account? {
        return transaction(Database.connect(dataSource)) {
            Accounts.select { Accounts.accountNumber eq accountNumber }
                    .map { Accounts.toDomain(it) }
                    .firstOrNull()
        }
    }

    fun findByPhoneNumber(phoneNumber: String): Account? {
        return transaction(Database.connect(dataSource)) {
            Accounts.select { Accounts.phoneNumber eq phoneNumber }
                    .map { Accounts.toDomain(it) }
                    .firstOrNull()
        }
    }

    fun create(account: Account): Account? {
        transaction(Database.connect(dataSource)) {
            Accounts.insert { row ->
                row[accountNumber] = account.accountNumber!!
                row[firstName] = account.firstName!!
                row[lastName] = account.lastName!!
                row[phoneNumber] = account.phoneNumber!!
                row[balance] = account.balance!!

            }
        }
        return findByAccountNumber(account.accountNumber!!)
    }

    fun findAll(limit: Int = 0, offset: Int = 50): List<Account> {
        return transaction(Database.connect(dataSource)) {
            Accounts.selectAll().map { Accounts.toDomain(it) }
        }
    }

    fun delete(accountNumber: String) {
        transaction(Database.connect(dataSource)) {
            Accounts.deleteWhere { Accounts.accountNumber eq accountNumber }
        }
    }

    fun updateBalance(accountNumber: String, amount: BigDecimal) {
        transaction(Database.connect(dataSource)) {
            Accounts.update({ Accounts.accountNumber eq accountNumber }) { row ->
                if (amount.signum() > 0)
                    row[balance] = amount
            }
        }
    }
}