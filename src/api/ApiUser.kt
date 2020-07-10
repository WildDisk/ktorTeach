package ru.wilddisk.api

import data.model.UserRegistering
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import data.service.UserRepository
import ru.wilddisk.data.service.UserRespond

/**
 * API service to take info about users
 */
fun Application.apiUser() {
    routing {
        authenticate {
            route("/api/") {
                get("users") {
                    call.respond(UserRepository().allUsers())
                }
                get("user/{id}") {
                    val id = call.parameters["id"]
                    try {
                        call.respond(
                            UserRespond(
                                try {
                                    UserRegistering(id = id?.toLong() ?: -1)
                                } catch (e: NumberFormatException) {
                                    when {
                                        UserRegistering(username = id.toString()).find().id > (-1).toLong() ->
                                            UserRegistering(username = id.toString())
                                        UserRegistering(email = id.toString()).find().id > (-1).toLong() ->
                                            UserRegistering(email = id.toString())
                                        else -> UserRegistering(id = -1)
                                    }
                                }
                            ).find()
                        )
                    } catch (e: Exception) {
                        call.respond(mapOf(e.localizedMessage to false))
                    }
                }
                post("user") {
                    try {
                        val post = call.receive<UserRegistering>()
                        UserRegistering(
                            post.username,
                            post.password,
                            post.firstName,
                            post.lastName,
                            post.email
                        ).save()
                        call.respond(mapOf("ok" to true))
                    } catch (e: Exception) {
                        call.respond(mapOf(e.localizedMessage to false))
                    }
                }
            }
        }
    }
}