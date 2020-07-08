package ru.wilddisk.jwtConfig

import data.model.User
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing

/**
 * Web token generation
 */
fun Application.generateToken() {
    routing {
        route("/api/") {
            post("generate_token") {
                val user = call.receive<User>()
                val token = JwtConfig.generateToken(user)
                call.respond(token)
            }
        }
    }
}