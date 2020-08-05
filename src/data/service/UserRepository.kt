package data.service

import data.entity.Roles
import data.entity.Users
import data.model.Role
import data.model.UserRegistering
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.wilddisk.auth.AccessDenied

class UserRepository(private val userRegistering: UserRegistering) {
    fun allUsers(): List<UserRegistering> {
        val users = mutableListOf<UserRegistering>()
        if (userRegistering.role.contains(Role.ADMIN)) transaction {
            Users.selectAll().forEach { user ->
                val roles = mutableSetOf<Role>()
                transaction {
                    (Users leftJoin Roles).slice(Roles.role)
                        .select { Roles.userId eq user[Users.id] }
                        .forEach { role ->
                            roles.add(Role.valueOf(role[Roles.role]))
                        }
                    users.add(
                        UserRegistering(
                            user[Users.id],
                            user[Users.username],
                            user[Users.password],
                            user[Users.firstName],
                            user[Users.lastName],
                            user[Users.email],
                            roles
                        )
                    )
                }
            }
        } else throw AccessDenied
        return users
    }
}