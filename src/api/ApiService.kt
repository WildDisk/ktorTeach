package ru.wilddisk.api

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import ru.wilddisk.jwtConfig.jwtApplication
import data.model.User
import org.jetbrains.exposed.sql.transactions.transaction
import ru.wilddisk.data.getAllUsers

/**
 * API service to take info about users
 */
fun Application.api() {
    jwtApplication()
    transaction {  }
    routing {
        authenticate {
            route("/api/") {
                get("users") {
                    call.respond(getAllUsers())
                }
                get("user/{id}") {
                    try {
                        val id = call.parameters["id"]?.toInt() ?: 0
                        call.respond(getAllUsers()[id])
                    } catch (e: IndexOutOfBoundsException) {
                        call.respond(getAllUsers())
                    }
                }
                post("user") {
                    val post = call.receive<User>()
                    val user = User.Build()
                        .username(post.username)
                        .password(post.password)
                        .firstName(post.firstName)
                        .lastName(post.lastName)
                        .email(post.email)
                        .build()
                    user.createUser()
                    call.respond(mapOf("ok" to true))
                }
            }
        }
    }
}