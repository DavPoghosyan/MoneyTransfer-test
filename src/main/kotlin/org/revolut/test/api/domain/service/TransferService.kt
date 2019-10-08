package org.revolut.test.api.domain.service

import org.revolut.test.api.domain.Transfer
import org.revolut.test.api.domain.repository.TransferRepository

class TransferService(private val accountService: AccountService, private val transferRepository: TransferRepository) {


    fun execute(transfer: Transfer) {
        val senderCurrentBalance = accountService.getBalance(transfer.sender)
        if(!accountService.isAccountExistsAndActive(transfer.sender)) {
            transfer.message = "sender's account is not active"
        } else if(!accountService.isAccountExistsAndActive(transfer.receiver)) {
            transfer.message = "receiver not found"
        } else if(senderCurrentBalance < transfer.amount) {
            transfer.message = "not enough amount"
        } else {
            val receiverCurrentBalance = accountService.getBalance(transfer.receiver)
            accountService.updateBalance(transfer.sender,senderCurrentBalance.minus(transfer.amount))
            accountService.updateBalance(transfer.receiver,receiverCurrentBalance.add(transfer.amount))
            transfer.isExecuted = true
        }
        transferRepository.save(transfer)

    }
}