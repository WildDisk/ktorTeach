package ru.wilddisk

import freemarker.cache.ClassTemplateLoader
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.freemarker.FreeMarker
import io.ktor.gson.gson
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import ru.wilddisk.controller.hello
import ru.wilddisk.controller.json
import ru.wilddisk.model.User
import java.time.LocalDateTime



val jsonResponse = User("userName", "userName@email.com", LocalDateTime.now())

val users = mutableListOf(jsonResponse)

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
        json()
    }.start(true)
}