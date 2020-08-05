package ru.wilddisk.api

import data.model.UserRegistering
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
import ru.wilddisk.data.model.UserMessage
import ru.wilddisk.data.service.MessageRepository
import ru.wilddisk.data.service.MessageRespond
import ru.wilddisk.data.service.UserRespond

/**
 * Api service for message
 */
fun Application.apiMessage() {
    routing {
        authenticate {
            route("/api/") {
                /**
                 * send message
                 */
                post("/message") {
                    try {
                        MessageRespond(
                            UserRespond(
                                call.authentication.principal() ?: UserRegistering()
                            ).find(),
                            UserMessage(message = call.receive<UserMessage>().message)
                        ).save()
                        call.respond(mapOf("ok" to true))
                    } catch (e: Exception) {
                        call.respond(mapOf(e.localizedMessage to false))
                    }
                }
                /**
                 * update status message
                 * for user who have role ADMIN
                 */
                post("/status_message_update") {
                    try {
                        val message = call.receive<UserMessage>()
                        MessageRespond(
                            UserRespond(
                                call.authentication.principal() ?: UserRegistering()
                            ).find(),
                            UserMessage(
                                messageId = message.messageId,
                                message = message.message,
                                messageStatus = message.messageStatus
                            )
                        ).update()
                        call.respond(mapOf("ok" to true))
                    } catch (e: Exception) {
                        call.respond(mapOf(e.localizedMessage to false))
                    }
                }
                /**
                 * message on id
                 */
                get("/message/{id}") {
                    val id = call.parameters["id"]?.toLong() ?: -1
                    try {
                        call.respond(
                            MessageRespond(
                                UserRespond(
                                    call.authentication.principal() ?: UserRegistering()
                                ).find(),
                                UserMessage(id)
                            ).find()
                        )
                    } catch (e: Exception) {
                        call.respond(mapOf(e.localizedMessage to false))
                    }
                }
                /**
                 * all messages
                 */
                get("/messages") {
                    try {
                        call.respond(
                            MessageRepository(
                                call.authentication.principal() ?: UserRegistering()
                            ).allMessages()
                        )
                    } catch (e: Exception) {
                        call.respond(mapOf(e.localizedMessage to false))
                    }
                }
            }
        }
    }
}