package org.revolut.test.api

import org.revolut.test.api.config.AppConfig
import org.h2.tools.Server

fun main(args: Array<String>) {
    Server.createWebServer().start()
    AppConfig().setup().start()
}
