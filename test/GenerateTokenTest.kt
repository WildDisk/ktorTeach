package ru.wilddisk

import data.model.User
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication
import ru.wilddisk.jwtConfig.JwtConfig
import ru.wilddisk.jwtConfig.generateToken
import kotlin.test.assertEquals

class GenerateTokenTest {
    fun `Generate token`(): Unit = withTestApplication {
        application.install(ContentNegotiation) {
            gson { setPrettyPrinting() }
        }
        application.generateToken()
        handleRequest(HttpMethod.Post, "/api/generate_token") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            setBody("{\"username\":\"username\",\"password\":\"password\"}")
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(JwtConfig.generateToken(User("username", "password")), response.content)
        }
    }
}