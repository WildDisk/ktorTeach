package data.model

import data.entity.Roles
import data.entity.Users
import data.service.RolesForUser
import data.service.User
import io.ktor.auth.Principal
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.wilddisk.auth.BcryptHash
import ru.wilddisk.model.Role

data class UserRegistering(
    val id: Long = -1,
    val username: String = "",
    val password: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val role: Set<Role> = setOf(Role.USER)
) : Principal, User {
    constructor(username: String, password: String) : this(
        id = -1,
        username = username,
        password = password
    )

    constructor(
        username: String,
        password: String,
        firstName: String,
        lastName: String,
        email: String
    ) : this(
        id = -1,
        username = username,
        password = password,
        firstName = firstName,
        lastName = lastName,
        email = email
    )

    override fun find(): UserRegistering {
        var desiredUser = UserRegistering()
        when {
            id > (-1).toLong() -> transaction {
                Users.select { Users.id eq id }.toList()
            }
            username != "" -> transaction {
                Users.select { Users.username eq username }.toList()
            }
            email != "" -> transaction {
                Users.select { Users.email eq email }.toList()
            }
            else -> listOf()
        }.forEach {
            desiredUser = UserRegistering(
                it[Users.id],
                it[Users.username],
                it[Users.password],
                it[Users.firstName],
                it[Users.lastName],
                it[Users.email],
                RolesForUser(this).roles()
            )
        }
        return desiredUser
    }

    override fun save() {
        when {
            this.username.isEmpty() -> throw RuntimeException("Username must not be empty!")
            this.password.isEmpty() -> throw RuntimeException("Password must not be empty!")
            this.email.isEmpty() -> throw RuntimeException("Password must not be empty!")
            else -> {
                transaction {
                    Users.insert {
                        it[username] = this@UserRegistering.username
                        it[password] = BcryptHash.hashPassword(this@UserRegistering.password)
                        it[firstName] = this@UserRegistering.firstName
                        it[lastName] = this@UserRegistering.lastName
                        it[email] = this@UserRegistering.email
                    }
                }
                this.role.forEach { roleItem ->
                    transaction {
                        Roles.insert {
                            it[userId] =
                                Users.select { Users.username eq this@UserRegistering.username }.single()[Users.id]
                            it[role] = roleItem.name
                        }
                    }
                }
            }
        }
    }

    override fun update() {}
}