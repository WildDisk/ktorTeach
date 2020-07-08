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
import ru.wilddisk.api.api
import ru.wilddisk.jwtConfig.JwtConfig
import kotlin.test.assertEquals

class UserRegisterFailedTest {
    fun `trying register a new user without username, password or email`(body: String): String? {
        var exceptionFailed: String? = ""
        withTestApplication {
            application.install(ContentNegotiation) {
                gson { setPrettyPrinting() }
            }
            application.api()
            handleRequest(HttpMethod.Post, "/api/user") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader(HttpHeaders.Authorization, "Bearer ${JwtConfig.generateToken(User("admin", "admin"))}")
                setBody(body)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                exceptionFailed = response.content
            }
        }
        return exceptionFailed
    }
}