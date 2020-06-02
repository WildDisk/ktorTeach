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
import ru.wilddisk.model.User
import ru.wilddisk.users
import java.time.LocalDateTime

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
            users.add(User(postValue["name"].toString(), postValue["email"].toString(), LocalDateTime.now()))
            call.respondRedirect("/")
        }
    }
}

/**
 * returns json objects
 */
fun Application.json() {
    routing {
        get("/json") {
            call.respond(users)
        }
    }
}