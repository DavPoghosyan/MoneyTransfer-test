package org.revolut.test.api.web.controllers

import io.javalin.Javalin
import io.javalin.util.HttpUtil
import org.revolut.test.api.config.AppConfig
import org.revolut.test.api.domain.AccountDTO
import org.eclipse.jetty.http.HttpStatus
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AccountControllerTest {
    private lateinit var app: Javalin
    private lateinit var http: HttpUtil

    @Before
    fun start() {
        app = AppConfig().setup().start()
        http = HttpUtil(app.port())
    }

    @After
    fun stop() {
        app.stop()
    }

    @Test
    fun `get account by account number`() {
        val accountNumber = "TEST0001"

        val response = http.get<AccountDTO>("/api/accounts/$accountNumber")

        assertEquals(response.status, HttpStatus.OK_200)
        assertEquals(response.body.account?.accountNumber, accountNumber)
    }

}