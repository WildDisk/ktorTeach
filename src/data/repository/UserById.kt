package ru.wilddisk.data.repository

import data.entity.Users
import data.model.User
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class UserById(
    private val user: User = User()
): IUser by user {
    override fun find(): User? {
        var desiredUser: User = user
        val roles = RolesForUser(user).roles()
        transaction {
            Users.select { Users.id eq (user.id ?: -1) }.toList()
        }.forEach {
            desiredUser = User(
                it[Users.id],
                it[Users.username],
                it[Users.password],
                it[Users.firstName],
                it[Users.lastName],
                it[Users.email],
                roles
            )
        }
        return if (desiredUser.id != null) desiredUser else null
    }
}