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
import ru.wilddisk.model.User
import ru.wilddisk.util.users
import java.time.LocalDateTime

/**
 * API service to take info about users
 */
fun Application.api() {
    jwtApplication()
    routing {
        authenticate {
            route("/api/") {
                get("users") {
                    call.respond(users)
                }
                get("user/{id}") {
                    try {
                        val id = call.parameters["id"]?.toInt() ?: 0
                        call.respond(users[id])
                    } catch (e: IndexOutOfBoundsException) {
                        call.respond(users[0])
                    }
                }
                post("user") {
                    val post = call.receive<User>()
                    users.add(
                        User(
                            post.name,
                            post.password,
                            post.email ?: "email is absent",
                            post.date ?: LocalDateTime.now()
                        )
                    )
                    call.respond(mapOf("ok" to true))
                }
            }
        }
    }
}