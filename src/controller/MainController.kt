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
                    mapOf("user" to UserRepository().allUsers()[0], "users" to UserRepository().allUsers())
                )
            )
        }
        post {
            val postValue = call.receiveParameters()
            users.add(
                UserRegistering(
                    postValue["username"].toString(),
                    postValue["password"].toString(),
                    postValue["firstName"].toString(),
                    postValue["lastName"].toString(),
                    postValue["email"].toString()
                )
            )
            call.respondRedirect("/")
        }
    }
}

val users = UserRepository().allUsers().toMutableList()
