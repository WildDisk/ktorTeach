package ru.wilddisk

import com.google.gson.Gson
import data.model.Role
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
import ru.wilddisk.api.apiUser
import ru.wilddisk.data.entity.tables
import ru.wilddisk.jwtConfig.JwtConfig
import ru.wilddisk.jwtConfig.jwtApplication
import kotlin.test.assertEquals

class UserRegisteringTest {
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
    fun `Create a new user`(): Unit = withTestApplication {
        application.install(ContentNegotiation) {
            gson { setPrettyPrinting() }
        }
        application.jwtApplication()
        application.apiUser()
        handleRequest(HttpMethod.Post, "/api/user") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.Authorization, "Bearer ${JwtConfig.generateToken(UserRegistering("admin", "admin"))}")
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

    @Test
    fun `Failed create a new user, because not have username`(): Unit = withTestApplication {
        application.install(ContentNegotiation) {
            gson { setPrettyPrinting() }
        }
        application.jwtApplication()
        application.apiUser()
        handleRequest(HttpMethod.Post, "/api/user") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.Authorization, "Bearer ${JwtConfig.generateToken(UserRegistering("admin", "admin"))}")
            setBody("""
                {
                    "username":"",
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
                    mapOf("Username must not be empty!" to false)
                ).toString().replace("\n", " ").replace(" ", ""),
                response.content?.replace("\n", "")?.replace(" ", "")
            )
        }
    }

    @Test
    fun `Failed create a new user, because not have password`(): Unit = withTestApplication {
        application.install(ContentNegotiation) {
            gson { setPrettyPrinting() }
        }
        application.jwtApplication()
        application.apiUser()
        handleRequest(HttpMethod.Post, "/api/user") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.Authorization, "Bearer ${JwtConfig.generateToken(UserRegistering("admin", "admin"))}")
            setBody("""
                {
                    "username":"Василий",
                    "password":"",
                    "firstName":"Василий",
                    "lastName":"Стрельников",
                    "email":"vasya@exmaple.com"
                }
            """.trimIndent())
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(
                Gson().toJson(
                    mapOf("Password must not be empty!" to false)
                ).toString().replace("\n", " ").replace(" ", ""),
                response.content?.replace("\n", "")?.replace(" ", "")
            )
        }
    }

    @Test
    fun `Failed create a new user, because not have email`(): Unit = withTestApplication {
        application.install(ContentNegotiation) {
            gson { setPrettyPrinting() }
        }
        application.jwtApplication()
        application.apiUser()
        handleRequest(HttpMethod.Post, "/api/user") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.Authorization, "Bearer ${JwtConfig.generateToken(UserRegistering("admin", "admin"))}")
            setBody("""
                {
                    "username":"Василий",
                    "password":"password",
                    "firstName":"Василий",
                    "lastName":"Стрельников",
                    "email":""
                }
            """.trimIndent())
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(
                Gson().toJson(
                    mapOf("User with that email already exists!" to false)
                ).toString().replace("\n", " ").replace(" ", ""),
                response.content?.replace("\n", "")?.replace(" ", "")
            )
        }
    }

    @Test
    fun `User with username already exists`(): Unit = withTestApplication {
        application.install(ContentNegotiation) {
            gson { setPrettyPrinting() }
        }
        application.jwtApplication()
        application.apiUser()
        handleRequest(HttpMethod.Post, "/api/user") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.Authorization, "Bearer ${JwtConfig.generateToken(UserRegistering("admin", "admin"))}")
            setBody("""
                {
                    "username":"admin",
                    "password":"password",
                    "firstName":"Василий",
                    "lastName":"Стрельников",
                    "email":"admin@example.com"
                }
            """.trimIndent())
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(
                Gson().toJson(
                    mapOf("User with that username already exists!" to false)
                ).toString().replace("\n", " ").replace(" ", ""),
                response.content?.replace("\n", "")?.replace(" ", "")
            )
        }
    }

    @Test
    fun `User with email already exists`(): Unit = withTestApplication {
        application.install(ContentNegotiation) {
            gson { setPrettyPrinting() }
        }
        application.jwtApplication()
        application.apiUser()
        handleRequest(HttpMethod.Post, "/api/user") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.Authorization, "Bearer ${JwtConfig.generateToken(UserRegistering("admin", "admin"))}")
            setBody("""
                {
                    "username":"admins",
                    "password":"password",
                    "firstName":"Василий",
                    "lastName":"Стрельников",
                    "email":"admin@example.com"
                }
            """.trimIndent())
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(
                Gson().toJson(
                    mapOf("User with that email already exists!" to false)
                ).toString().replace("\n", " ").replace(" ", ""),
                response.content?.replace("\n", "")?.replace(" ", "")
            )
        }
    }

    @Test
    fun `Find user by id`(): Unit = withTestApplication {
        application.install(ContentNegotiation) {
            gson { setPrettyPrinting() }
        }
        application.jwtApplication()
        application.apiUser()
        handleRequest(HttpMethod.Get, "/api/user/2") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.Authorization, "Bearer ${JwtConfig.generateToken(UserRegistering("admin", "admin"))}")
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(
                Gson().toJson(
                    UserRegistering(id = 2).find()
                ).toString().replace("\n", " ").replace(" ", ""),
                response.content?.replace("\n", "")?.replace(" ", "")
            )
        }
    }

    @Test
    fun `Find user by username`(): Unit = withTestApplication {
        application.install(ContentNegotiation) {
            gson { setPrettyPrinting() }
        }
        application.jwtApplication()
        application.apiUser()
        handleRequest(HttpMethod.Get, "/api/user/newUser") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.Authorization, "Bearer ${JwtConfig.generateToken(UserRegistering("admin", "admin"))}")
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(
                Gson().toJson(
                    UserRegistering(username = "newUser").find()
                ).toString().replace("\n", " ").replace(" ", ""),
                response.content?.replace("\n", "")?.replace(" ", "")
            )
        }
    }

    @Test
    fun `Find user by email`(): Unit = withTestApplication {
        application.install(ContentNegotiation) {
            gson { setPrettyPrinting() }
        }
        application.jwtApplication()
        application.apiUser()
        handleRequest(HttpMethod.Get, "/api/user/admin@example.com") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.Authorization, "Bearer ${JwtConfig.generateToken(UserRegistering("admin", "admin"))}")
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(
                Gson().toJson(
                    UserRegistering(email = "admin@example.com").find()
                ).toString().replace("\n", " ").replace(" ", ""),
                response.content?.replace("\n", "")?.replace(" ", "")
            )
        }
    }

    @Test
    fun `User not found`(): Unit = withTestApplication {
        application.install(ContentNegotiation) {
            gson { setPrettyPrinting() }
        }
        application.jwtApplication()
        application.apiUser()
        handleRequest(HttpMethod.Get, "/api/user/notFoundUser") {
            addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            addHeader(HttpHeaders.Authorization, "Bearer ${JwtConfig.generateToken(UserRegistering("admin", "admin"))}")
        }.apply {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals(
                Gson().toJson(
                    mapOf("User not found!" to false)
                ).toString().replace("\n", " ").replace(" ", ""),
                response.content?.replace("\n", "")?.replace(" ", "")
            )
        }
    }
}