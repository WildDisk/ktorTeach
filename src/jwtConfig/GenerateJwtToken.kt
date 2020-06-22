package ru.wilddisk.jwtConfig

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.post
import io.ktor.routing.route
import io.ktor.routing.routing
import ru.wilddisk.model.User

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