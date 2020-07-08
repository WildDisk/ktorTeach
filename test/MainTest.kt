package ru.wilddisk

import com.google.gson.Gson
import data.model.User
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Before
import org.junit.Test
import ru.wilddisk.data.entity.tables
import ru.wilddisk.data.repository.UserSave
import ru.wilddisk.model.Role
import kotlin.test.assertEquals

class MainTest {
    private val firstUser = User(null, "admin", "admin", null, null, "admin@example.com", setOf(Role.ADMIN, Role.USER))
    private val secondUser = User("username", "password", null, null, "username@example.com")
    private val thirdUser =
        User(null, "newUser", "newuserpassword", "firstNameNewUser", "lastNameNewUser", "newuser@example.com")
    private val availableUsers = listOf(firstUser, secondUser, thirdUser)

    @Before
    fun before() {
        Setup()
        transaction {
            SchemaUtils.drop(*tables)
            SchemaUtils.create(*tables)
        }
        availableUsers.forEach {
            UserSave(it).save()
        }
    }

    @Test
    fun `JWT generation token`() {
        GenerateTokenTest().`Generate token`()
    }

    @Test
    fun `Find user by id`() {
        UserFindTest().`Find user`(1)
    }

    @Test
    fun `User registration`() {
        UserRegisterTest().`Create new user`()
    }

    @Test
    fun `Failed try register user`() {
        UserRegisterFailedTest().apply {
            assertEquals(
                Gson().toJson(mapOf("Username must not be empty!" to false))
                    .toString()
                    .replace("\n", "")
                    .replace(" ", ""),
                `trying register a new user without username, password or email`("{\"username\":\"\",\"password\":\"password\",\"email\":\"email@example.com\"}")
                    ?.replace("\n", "")
                    ?.replace(" ", "")
            )
            assertEquals(
                Gson().toJson(mapOf("Password must not be empty!" to false))
                    .toString()
                    .replace("\n", "")
                    .replace(" ", ""),
                `trying register a new user without username, password or email`("{\"username\":\"username\",\"password\":\"\",\"email\":\"email@example.com\"}")
                    ?.replace("\n", "")
                    ?.replace(" ", "")
            )
            assertEquals(
                Gson().toJson(mapOf("Email must not be empty!" to false))
                    .toString()
                    .replace("\n", "")
                    .replace(" ", ""),
                `trying register a new user without username, password or email`("{\"username\":\"username\",\"password\":\"password\",\"email\":\"\"}")
                    ?.replace("\n", "")
                    ?.replace(" ", "")
            )
        }
    }

    @Test
    fun `Test Freemarker template`() {
        HelloTest().`Find rows in FreeMarker template`()
    }
}