package ru.wilddisk.controller

import data.model.UserRegistering
import data.service.UserRepository
import freemarker.cache.ClassTemplateLoader
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import ru.wilddisk.data.service.UserRespond

/**
 * Function responsible for using the freemarker template and sending entrance data
 */
fun Application.hello() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }
    routing {
        get("/") {
            call.respond(
                FreeMarkerContent(
                    "hello.ftl",
                    mapOf(
                        "user" to UserRegistering(id = 2).find(),
                        "users" to UserRepository(UserRegistering(id = 1).find()).allUsers()
                    )
                )
            )
        }
        post {
            val postValue = call.receiveParameters()
            try {
                UserRespond(
                    UserRegistering(
                        username = postValue["username"] ?: "",
                        password = postValue["password"] ?: "",
                        firstName = postValue["firstName"] ?: "",
                        lastName = postValue["lastName"] ?: "",
                        email = postValue["email"] ?: ""
                    )
                ).save()
                call.respondRedirect("/")
            } catch (e: Exception) {
                call.respond(e.localizedMessage)
            }
        }
    }
}