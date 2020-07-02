package ru.wilddisk.api

import data.model.User
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import org.jetbrains.exposed.sql.transactions.transaction
import ru.wilddisk.data.repository.UserRepository
import ru.wilddisk.jwtConfig.jwtApplication

/**
 * API service to take info about users
 */
fun Application.api() {
    jwtApplication()
    transaction { }
    routing {
        authenticate {
            route("/api/") {
                get("users") {
                    call.respond(UserRepository().allUsers())
                }
                get("user/{id}") {
                    val id = call.parameters["id"]?.toLong() ?: 0
                    call.respond(User().user(id) ?: mapOf("User not found" to false))
                }
                post("user") {
                    try {
                        val post = call.receive<User>()
                        User.Build()
                            .username(post.username)
                            .password(post.password)
                            .firstName(post.firstName)
                            .lastName(post.lastName)
                            .email(post.email)
                            .build()
                            .createUser()
                        call.respond(mapOf("ok" to true))
                    } catch (e: Exception) {
                        call.respond(mapOf(e.localizedMessage to false))
                    }
                }
                get("get_string") {
                    call.respond("Some empty string")
                }
            }
        }
    }
}