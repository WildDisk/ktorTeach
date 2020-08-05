package ru.wilddisk.api

import data.model.UserRegistering
import data.service.UserRepository
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import ru.wilddisk.data.service.UserRespond

/**
 * API service to take info about users
 */
fun Application.apiUser() {
    routing {
        route("/api/") {
            /**
             * create new user
             */
            post("user") {
                try {
                    val post = call.receive<UserRegistering>()
                    UserRespond(
                        UserRegistering(
                            username = post.username,
                            password = post.password,
                            firstName = post.firstName,
                            lastName = post.lastName,
                            email = post.email
                        )
                    ).save()
                    call.respond(mapOf("ok" to true))
                } catch (e: Exception) {
                    call.respond(mapOf(e.localizedMessage to false))
                }
            }
            authenticate {
                /**
                 * all users only ADMIN
                 */
                get("users") {
                    try {
                        call.respond(
                            UserRepository(
                                UserRespond(
                                    call.authentication.principal() ?: UserRegistering()
                                ).find()
                            ).allUsers()
                        )
                    } catch (e: Exception) {
                        call.respond(mapOf(e.localizedMessage to false))
                    }
                }
                /**
                 * user profile on id
                 */
                get("user/{id}") {
                    val id = call.parameters["id"] ?: ""
                    try {
                        call.respond(UserRespond().findOnId(id))
                    } catch (e: Exception) {
                        call.respond(mapOf(e.localizedMessage to false))
                    }
                }
                /**
                 * update user profile authorization user
                 */
                post("user_profile_update") {
                    try {
                        val post = call.receive<UserRegistering>()
                        UserRespond(
                            call.authentication.principal() ?: UserRegistering()
                        ).update(
                            UserRegistering(
                                username = post.username,
                                password = post.password,
                                firstName = post.firstName,
                                lastName = post.lastName,
                                email = post.email
                            )
                        )
                        call.respond(mapOf("ok" to true))
                    } catch (e: Exception) {
                        call.respond(mapOf(e.localizedMessage to false))
                    }
                }
                /**
                 * edit users profile for admin
                 */
                post("update_user_profile_admin") {
                    try {
                        val post = call.receive<UserRegistering>()
                        UserRespond(
                            call.authentication.principal() ?: UserRegistering()
                        ).updateUserProfileAdmin(
                            UserRegistering(
                                id = post.id,
                                password = post.password,
                                firstName = post.firstName,
                                lastName = post.lastName,
                                email = post.email
                            )
                        )
                        call.respond(mapOf("ok" to true))
                    } catch (e: Exception) {
                        call.respond(mapOf(e.localizedMessage to false))
                    }
                }
            }
        }
    }
}