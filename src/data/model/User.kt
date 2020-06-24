package data.model

import io.ktor.auth.Principal
import data.entity.Users
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import ru.wilddisk.model.Role

data class User(
    val username: String,
    val password: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val role: MutableSet<Role> = mutableSetOf(Role.USER)
) : Principal {
    private var id: Long? = null
    private fun id(): Long? = id
    private fun id(id: Long) {
        this.id = id
    }
    data class Build(
        private var id: Long? = null,
        private var username: String? = null,
        private var password: String? = null,
        private var firstName: String? = null,
        private var lastName: String? = null,
        private var email: String? = null,
        private var role: MutableSet<Role>? = null
    ) {
        fun id(id: Long?) = apply { this.id = id }
        fun username(username: String?) = apply { this.username = username }
        fun password(password: String?) = apply { this.password = password }
        fun firstName(firstName: String?) = apply { this.firstName = firstName }
        fun lastName(lastName: String?) = apply { this.lastName = lastName }
        fun email(email: String?) = apply { this.email = email }
        fun role(role: MutableSet<Role>?) = apply { this.role = role }
        fun build() = User(
            username.toString(),
            password.toString(),
            firstName.toString(),
            lastName.toString(),
            email.toString(),
            role ?: mutableSetOf()
        )
    }
    fun createUser() {
        val user = this
        transaction {
            Users.insert {
                it[username] = user.username
                it[password] = user.password
                it[firstName] = user.firstName ?: ""
                it[lastName] = user.lastName ?: ""
                it[email] = user.email ?: ""
            }
        }
    }
}