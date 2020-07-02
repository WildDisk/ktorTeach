package data.model

import data.entity.Users
import io.ktor.auth.Principal
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.wilddisk.migration.Roles
import ru.wilddisk.model.Role

class User(
    private val id: Long? = null,
    val username: String,
    val password: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String,
    val role: Set<Role> = setOf(Role.USER)
) : Principal {
    constructor() : this(
        id = null,
        username = "",
        password = "",
        firstName = null,
        lastName = null,
        email = ""
    )

    data class Build(
        private var id: Long? = null,
        private var username: String? = null,
        private var password: String? = null,
        private var firstName: String? = null,
        private var lastName: String? = null,
        private var email: String? = null,
        private var role: Set<Role>? = null
    ) {
        fun id(id: Long?) = apply { this.id = id }
        fun username(username: String?) = apply { this.username = username }
        fun password(password: String?) = apply { this.password = password }
        fun firstName(firstName: String?) = apply { this.firstName = firstName }
        fun lastName(lastName: String?) = apply { this.lastName = lastName }
        fun email(email: String?) = apply { this.email = email }
        fun role(role: Set<Role>?) = apply { this.role = role }
        fun build() = User(
            id,
            username.toString(),
            password.toString(),
            firstName.toString(),
            lastName.toString(),
            email.toString(),
            role ?: setOf()
        )
    }

    fun createUser() {
        val user = this
        when {
            username.isEmpty() -> throw RuntimeException("Username must not be empty!")
            password.isEmpty() -> throw RuntimeException("Password must not be empty!")
            email.isEmpty() -> throw RuntimeException("Email must not be empty!")
            else -> transaction {
                Users.insert {
                    it[username] = user.username
                    it[password] = user.password
                    it[firstName] = user.firstName ?: ""
                    it[lastName] = user.lastName ?: ""
                    it[email] = user.email
                }
                Roles.insert {
                    it[userId] = Users.select { Users.username eq user.username }.single()[Users.id]
                    it[role] = Role.USER.toString()
                }
            }
        }
    }

    fun user(userId: Long = 0): User? {
        var user = this
        val roles = this.roles(userId)
        transaction {
            Users.select { Users.id eq userId }.toList()
        }.forEach {
            user = Build()
                .id(it[Users.id])
                .username(it[Users.username])
                .password(it[Users.password])
                .firstName(it[Users.firstName])
                .lastName(it[Users.lastName])
                .email(it[Users.email])
                .role(roles)
                .build()
        }
        return if (user.id != null) user
        else null
    }

    private fun roles(userId: Long): Set<Role> {
        val list = mutableSetOf<Role>()
        transaction {
            (Users leftJoin Roles)
                .select { Users.id eq Roles.userId }
                .andWhere { Users.id eq userId }.toList()
        }.forEach {
            list.add(Role.valueOf(it[Roles.role]))
        }
        return list
    }
}