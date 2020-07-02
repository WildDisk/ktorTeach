package ru.wilddisk

import freemarker.cache.ClassTemplateLoader
import io.ktor.application.install
import io.ktor.features.CORS
import io.ktor.features.ContentNegotiation
import io.ktor.freemarker.FreeMarker
import io.ktor.gson.gson
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import ru.wilddisk.api.api
import ru.wilddisk.controller.hello
import ru.wilddisk.data.db.connectionDatabase
import ru.wilddisk.jwtConfig.generateToken


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
        install(FreeMarker) {
            templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
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
        connectionDatabase()
        hello()
        api()
        generateToken()
    }.start(true)
}