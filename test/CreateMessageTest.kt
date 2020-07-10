package ru.wilddisk

import com.google.gson.Gson
import data.model.UserRegistering
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
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Before
import org.junit.Test
import ru.wilddisk.api.apiMessage
import ru.wilddisk.data.entity.tables
import ru.wilddisk.jwtConfig.JwtConfig
import ru.wilddisk.jwtConfig.jwtApplication
import ru.wilddisk.model.Role
import kotlin.test.assertEquals

class CreateMessageTest {
    private val firstUser = UserRegistering(-1, "admin", "admin", "", "", "admin@example.com", setOf(Role.ADMIN, Role.USER))
    private val secondUser = UserRegistering("username", "password", "", "", "username@example.com")
    private val thirdUser =
        UserRegistering(-1, "newUser", "newuserpassword", "firstNameNewUser", "lastNameNewUser", "newuser@example.com")
    private val availableUsers = listOf(firstUser, secondUser, thirdUser)

    @Before
    fun before() {
        Setup
        transaction {
            SchemaUtils.drop(*tables)
            SchemaUtils.create(*tables)
        }
        availableUsers.forEach {
            it.save()
        }
    }

    @Test
    fun `Create a new message`(): Unit = withTestApplication {
        application.install(ContentNegotiation) {
            gson { setPrettyPrinting() }
        }
        application.jwtApplication()
        application.apiMessage()
        handleRequest(HttpMethod.Post, "/api/message") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.Authorization, "Bearer ${JwtConfig.generateToken(UserRegistering("admin", "admin"))}")
            setBody("""
                {"message":"some a new message"}
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