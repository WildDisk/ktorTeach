package ru.wilddisk

import com.google.gson.Gson
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

class UserRegisterTest {
    fun `Create new user`(): Unit = withTestApplication {
        application.install(ContentNegotiation) {
            gson { setPrettyPrinting() }
        }
        application.api()
        handleRequest(HttpMethod.Post, "/api/user") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.Authorization, "Bearer ${JwtConfig.generateToken(User("admin", "admin"))}")
            setBody("""
                {
                    "username":"Василий",
                    "password":"password",
                    "firstName":"Василий",
                    "lastName":"Стрельников",
                    "email":"vasya@exmaple.com"
                }
            """.trimIndent())
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(
                Gson().toJson(
                    mapOf("ok" to true)
                ).toString().replace("\n", " ").replace(" ", ""),
                response.content?.replace("\n", "")?.replace(" ", "")
            )
        }
    }
}