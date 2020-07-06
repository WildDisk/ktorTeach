package ru.wilddisk.data.repository

import data.entity.Users
import data.model.User
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.wilddisk.migration.Roles
import ru.wilddisk.model.Role

class UserSave(private val user: User) : IUser by user {
    override fun save() {
        when {
            user.username.isEmpty() -> throw RuntimeException("Username must not be empty!")
            user.password.isEmpty() -> throw RuntimeException("Password must not be empty!")
            else -> transaction {
                Users.insert {
                    it[username] = user.username
                    it[password] = user.password
                    it[firstName] = user.firstName ?: ""
                    it[lastName] = user.lastName ?: ""
                    it[email] = user.email ?: ""
                }
                Roles.insert {
                    it[userId] = Users.select { Users.username eq user.username }.single()[Users.id]
                    it[role] = Role.USER.toString()
                }
            }
        }
    }
}