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
import io.ktor.server.testing.withTestApplication
import org.junit.Test
import ru.wilddisk.api.api
import ru.wilddisk.data.repository.UserById
import ru.wilddisk.jwtConfig.JwtConfig
import kotlin.test.assertEquals

class UserFindTest {
    fun `Find user`(id: Long): Unit = withTestApplication {
        application.install(ContentNegotiation) {
            gson { setPrettyPrinting() }
        }
        application.api()
        handleRequest(HttpMethod.Get, "/api/user/$id") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.Authorization, "Bearer ${JwtConfig.generateToken(User("admin", "admin"))}")
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(
                Gson().toJson(UserById(User(id)).find()),
                response.content?.replace("\n", "")?.replace(" ", "")
            )
        }
    }
}