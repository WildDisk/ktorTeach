package ru.wilddisk.data.repository

import data.entity.Users
import data.model.User
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class UserById(private val userId: Long) {
    fun findUser(): User? {
        var user = User()
        val roles = RolesForUser(userId).roles()
        transaction {
            Users.select { Users.id eq userId }.toList()
        }.forEach {
            user = User(
                it[Users.id],
                it[Users.username],
                it[Users.password],
                it[Users.firstName],
                it[Users.lastName],
                it[Users.email],
                roles
            )
        }
        return if (user.id != null) user else null
    }
}