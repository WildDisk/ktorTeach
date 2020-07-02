package ru.wilddisk.data.repository

import data.entity.Users
import data.model.User
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.wilddisk.migration.Roles
import ru.wilddisk.model.Role

class UserRepository() {
    fun allUsers(): List<User> {
        val users = mutableListOf<User>()
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
                User.Build()
                    .id(user[Users.id])
                    .username(user[Users.username])
                    .password(user[Users.password])
                    .firstName(user[Users.firstName])
                    .lastName(user[Users.lastName])
                    .email(user[Users.email])
                    .role(roles)
                    .build()
            )
        }
        return users
    }
}