package org.revolut.test.api.web.controllers

import io.javalin.Context
import org.revolut.test.api.domain.AccountDTO
import org.revolut.test.api.domain.TransferDTO
import org.revolut.test.api.domain.service.AccountService
import java.math.BigDecimal

class AccountController(private val accountService: AccountService) {
    

    fun get(ctx: Context) {
        ctx.validatedPathParam("accountNumber")
                .check({ it.isNotBlank() })
                .getOrThrow().also { slug ->
                    accountService.findByAccountNumber(slug).apply {
                        ctx.json(AccountDTO(this))
                    }
                }
    }

    fun create(ctx: Context) {
        ctx.validatedBody<AccountDTO>()
                .check({ !it.account?.accountNumber.isNullOrBlank() })
                .check({ !it.account?.firstName.isNullOrBlank() })
                .check({ !it.account?.lastName.isNullOrBlank() })
                .check({ it.account?.balance?.signum()!! > 0})
                .getOrThrow().account?.also { account ->
            accountService.create(account).apply {
                ctx.json(AccountDTO(this))
            }
        }
    }


//
//    fun update(ctx: Context) {
//        val slug = ctx.validatedPathParam("slug").getOrThrow()
//        ctx.validatedBody<ArticleDTO>()
//                .check({ !it.article?.body.isNullOrBlank() })
//                .getOrThrow().article?.also { article ->
//            accountService.update(slug, article).apply {
//                ctx.json(ArticleDTO(this))
//            }
//        }
//    }
//
//    fun delete(ctx: Context) {
//        ctx.validatedPathParam("slug").getOrThrow().also { slug ->
//            accountService.delete(slug)
//        }
//    }
//
//    fun favorite(ctx: Context) {
//        ctx.validatedPathParam("slug").getOrThrow().also { slug ->
//            accountService.favorite(ctx.attribute("email"), slug).apply {
//                ctx.json(ArticleDTO(this))
//            }
//        }
//    }
//
//    fun unfavorite(ctx: Context) {
//        ctx.validatedPathParam("slug").getOrThrow().also { slug ->
//            accountService.unfavorite(ctx.attribute("email"), slug).apply {
//                ctx.json(ArticleDTO(this))
//            }
//        }
//    }
}