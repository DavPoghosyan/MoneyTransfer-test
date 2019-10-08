package org.revolut.test.api.web

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.delete
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.post
import io.javalin.apibuilder.ApiBuilder.put
import io.javalin.security.SecurityUtil.roles
import org.revolut.test.api.web.controllers.AccountController
import org.revolut.test.api.web.controllers.TransferController
import org.koin.standalone.KoinComponent

class Router(private val accountController: AccountController,
             private val transferController: TransferController) : KoinComponent {

    fun register(app: Javalin) {
        app.enableCaseSensitiveUrls()
        app.routes {
            path("accounts/:accountNumber") {
                get(accountController::get)
            }

            path("accounts") {
                post(accountController::create)
            }

            path("transfer") {
                put(transferController::transfer)
            }
        }
    }
}
