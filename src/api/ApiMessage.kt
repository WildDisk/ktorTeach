package ru.wilddisk.api

import data.model.UserRegistering
import data.service.MessageSave
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.auth.authentication
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import ru.wilddisk.data.model.Message
import ru.wilddisk.data.service.UpdateMessageStatus

/**
 * Api service for message
 */
fun Application.apiMessage() {
    routing {
        authenticate {
            route("/api/") {
                post("/message") {
                    try {
                        val authorizedUser = call.authentication.principal<UserRegistering>()
                        val message = call.receive<Message>().message
                        when {
                            authorizedUser == null -> throw RuntimeException("User not respond")
                            message == null -> throw RuntimeException("Message is empty")
                            else -> {
                                MessageSave(authorizedUser, message).save()
                                call.respond(mapOf("ok" to true))
                            }
                        }
                    } catch (e: Exception) {
                        call.respond(mapOf(e.localizedMessage to false))
                    }
                }
                post("/status_message_update") {
                    try {
                        val authorizedUser = call.authentication.principal<UserRegistering>()
                        val message = call.receive<Message>()
                        when {
                            authorizedUser == null -> throw RuntimeException("User not respond")
                            message.messageId == null -> throw RuntimeException("Message not respond")
                            message.messageStatus == "OPEN" -> {
                                UpdateMessageStatus(authorizedUser, message).update()
                                call.respond(mapOf("ok" to true))
                            }
                            message.messageStatus == "CLOSED" -> {
                                UpdateMessageStatus(authorizedUser, message).update()
                                call.respond(mapOf("ok" to true))
                            }
                        }
                    } catch (e: Exception) {
                        call.respond(mapOf(e.localizedMessage to false))
                    }
                }
            }
        }
    }
}