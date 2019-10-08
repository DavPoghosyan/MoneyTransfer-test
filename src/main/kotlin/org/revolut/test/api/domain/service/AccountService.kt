package org.revolut.test.api.domain.service

import com.github.slugify.Slugify
import io.javalin.BadRequestResponse
import io.javalin.InternalServerErrorResponse
import io.javalin.NotFoundResponse
import org.revolut.test.api.domain.Account
import org.revolut.test.api.domain.repository.AccountRepository
import java.math.BigDecimal
import java.time.temporal.TemporalAmount

class AccountService(private val accountRepository: AccountRepository) {

    fun findByAccountNumber(accountNumber: String): Account {
        return accountRepository.findByAccountNumber(accountNumber) ?: throw NotFoundResponse()
    }

    fun delete(accountNumber: String) {
        return accountRepository.delete(accountNumber)
    }

    fun findALL(limit: Int, offset: Int): List<Account> {
        return accountRepository.findAll(limit, offset)
    }

    fun create(account: Account): Account? {
        return accountRepository.create(account);
    }

    fun updateBalance(accountNumber: String, amount: BigDecimal) {
        accountRepository.updateBalance(accountNumber, amount)
    }


    fun getBalance(accountNumber: String): BigDecimal {
        return findByAccountNumber(accountNumber).balance
    }

    fun isAccountExistsAndActive(accountNumber: String): Boolean {
        return findByAccountNumber(accountNumber).isActive
    }

    fun topUp(accountNumber: String, amount: BigDecimal) {
        val currentBalance = getBalance(accountNumber)
        accountRepository.updateBalance(accountNumber, currentBalance.add(amount))
    }
}