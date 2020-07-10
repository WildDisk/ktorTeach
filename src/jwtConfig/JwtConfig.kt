package ru.wilddisk.jwtConfig

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import data.model.UserRegistering
import java.util.*

object JwtConfig {
    private const val secret = "my-secret" // use your own secret
    private const val issuer = "ru.wilddisk"  // use your own issuer
    private const val validityInMs = 36_000_00 * 24 // 1 day
    private val algorithm = Algorithm.HMAC512(secret)
    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()
    fun generateToken(userRegistering: UserRegistering): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("username", userRegistering.username)
        .withClaim("password", userRegistering.password)
        .withExpiresAt(getExpiration())
        .sign(algorithm)
    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)
}