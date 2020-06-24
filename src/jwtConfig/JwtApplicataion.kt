package ru.wilddisk.jwtConfig

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.jwt.jwt
import data.model.User

/**
 * Validation of web token when accessing api
 */
fun Application.jwtApplication() {
    install(Authentication) {
        jwt {
            verifier(JwtConfig.verifier)
            realm = "ru.wilddisk"
            validate { credentials ->
                val name = credentials.payload.getClaim("username").asString()
                val password = credentials.payload.getClaim("password").asString()
                if (name != null && password != null) {
                    User(name, password)
                } else {
                    null
                }
            }
        }
    }
}