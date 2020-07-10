package data.service

import data.entity.Users
import data.model.UserRegistering
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import data.entity.Roles
import ru.wilddisk.model.Role

class UserRepository {
    fun allUsers(): List<UserRegistering> {
        val users = mutableListOf<UserRegistering>()
        transaction {
            Users.selectAll().toList()
        }.forEach { user ->
            val roles = mutableSetOf<Role>()
            transaction {
                (Users leftJoin Roles).slice(Roles.role)
                    .select { Roles.userId eq user[Users.id] }
                    .toList()
            }.forEach { role ->
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
        return users
    }
}