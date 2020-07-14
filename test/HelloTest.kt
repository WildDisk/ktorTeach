package ru.wilddisk

import data.model.Role
import data.model.UserRegistering
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Before
import org.junit.Test
import ru.wilddisk.controller.hello
import ru.wilddisk.data.entity.tables
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HelloTest {
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
    fun `Find rows in FreeMarker template`(): Unit = withTestApplication {
        application.hello()
        handleRequest(HttpMethod.Get, "/") {
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertTrue { response.content!!.contains("Hello admin!") }
            assertTrue { response.content!!.contains("Username: admin") }
            assertTrue { response.content!!.contains("Email: admin@example.com") }
        }
    }
}