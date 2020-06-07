package ru.wilddisk.controller

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.request.receive
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
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
 * API
 */
fun Application.json() {
    routing {
        route("/api/") {
            get("users") {
                call.respond(users)
            }
            get("user/{id}") {
                val id = call.parameters["id"]?.toInt() ?: 0
                call.respond(users[id])
            }
            post("user") {
                val post = call.receive<User>()
                users.add(User(post.name, post.email, LocalDateTime.now()))
                call.respond(mapOf("ok" to true))
            }
        }
    }
}