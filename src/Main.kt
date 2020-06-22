package ru.wilddisk

import freemarker.cache.ClassTemplateLoader
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.freemarker.FreeMarker
import io.ktor.gson.gson
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import ru.wilddisk.api.api
import ru.wilddisk.controller.hello
import ru.wilddisk.jwtConfig.generateToken


/**
 * Creating embedded server on 8080 port
 */
fun main() {
    embeddedServer(Netty, 8080) {
        install(ContentNegotiation) {
            gson {
                setPrettyPrinting()
            }
        }
        install(FreeMarker) {
            templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
        }
        hello()
        api()
        generateToken()
    }.start(true)
}