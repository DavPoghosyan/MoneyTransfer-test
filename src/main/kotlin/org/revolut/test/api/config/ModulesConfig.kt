package org.revolut.test.api.config

import org.revolut.test.api.domain.service.AccountService
import org.revolut.test.api.domain.service.TransferService
import org.revolut.test.api.web.Router
import org.revolut.test.api.web.controllers.AccountController
import org.revolut.test.api.web.controllers.TransferController
import org.koin.dsl.module.module
import org.revolut.test.api.domain.repository.AccountRepository
import org.revolut.test.api.domain.repository.TransferRepository

object ModulesConfig {
    private val configModule = module {
        single { AppConfig() }
        single {
            DbConfig(getProperty("jdbc.url"), getProperty("db.username"), getProperty("db.password")).getDataSource()
        }
        single { Router(get(), get()) }
    }
    private val accountModule = module {
        single { AccountController(get()) }
        single { AccountService(get()) }
        single { AccountRepository(get()) }
    }

    private val transferModule = module {
        single { TransferController(get()) }
        single { TransferService(get(), get()) }
        single { TransferRepository(get()) }
    }
    internal val allModules = listOf(configModule,
            accountModule, transferModule)
}
