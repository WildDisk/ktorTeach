package ru.wilddisk.data

import data.model.User
import data.entity.Users
import io.ktor.application.Application
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.wilddisk.migration.Roles
import ru.wilddisk.model.Role

fun Application.getAllUsers(): List<User> {
    val users = mutableListOf<User>()
    transaction {
        Users.selectAll().forEach { user ->
            val roles = mutableSetOf<Role>()
            Roles.selectAll().forEach { role ->
                when {
                    user[Users.id] == role[Roles.userId] -> roles.add(Role.valueOf(role[Roles.role]))
                }
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
    }
    return users
}