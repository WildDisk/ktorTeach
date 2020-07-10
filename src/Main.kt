package ru.wilddisk

import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import ru.wilddisk.api.apiMessage
import ru.wilddisk.api.apiUser
import ru.wilddisk.controller.hello
import ru.wilddisk.data.db.ConnectionDatabase
import ru.wilddisk.jwtConfig.generateToken
import ru.wilddisk.jwtConfig.jwtApplication


/**
 * Creating embedded server on 9999 port
 */
fun main() {
    embeddedServer(Netty, 9999) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        install(CORS) {
            method(HttpMethod.Options)
            method(HttpMethod.Get)
            method(HttpMethod.Post)
            header(HttpHeaders.ContentType)
            header(HttpHeaders.Authorization)
            allowCredentials = true
            anyHost()
        }
        ConnectionDatabase
        jwtApplication()
        hello()
        apiUser()
        apiMessage()
        generateToken()
    }.start(true)
}