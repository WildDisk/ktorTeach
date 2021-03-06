package data.model

import data.entity.Roles
import data.entity.Users
import data.service.RolesForUser
import data.service.User
import io.ktor.auth.Principal
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.wilddisk.auth.BcryptHash

data class UserRegistering(
    val id: Long = -1,
    val username: String = "",
    val password: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val role: Set<Role> = setOf(Role.USER)
) : Principal, User {
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

    override fun update(profile: UserRegistering) {
        transaction {
            Users.update({ Users.id eq if (profile.id > -1) profile.id else id }) {
                it[password] = BcryptHash.hashPassword(profile.password)
                it[firstName] = profile.firstName
                it[lastName] = profile.lastName
                it[email] = profile.email
            }
        }
    }
}