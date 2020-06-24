package ru.wilddisk.controller

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import data.model.User
import ru.wilddisk.util.users

/**
 * Function responsible for using the freemarker template and sending entrance data
 */
fun Application.hello() {
    routing {
        get("/") {
            call.respond(FreeMarkerContent("hello.ftl", mapOf("user" to users[0], "users" to users)))
        }
        post {
            val postValue = call.receiveParameters()
            users.add(
                User.Build()
                    .username(postValue["username"])
                    .password(postValue["password"])
                    .firstName(postValue["firstName"])
                    .lastName(postValue["lastName"])
                    .email(postValue["email"])
                    .build()
            )
            call.respondRedirect("/")
        }
    }
}

