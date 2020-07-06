package ru.wilddisk.data.repository

import data.entity.Users
import data.model.User
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class UserByName(
    private val user: User = User("", "")
) : IUser by user {
    override fun find(): User? {
        var desiredUser = user
        val roles = RolesForUser(user).roles()
        transaction {
            Users.select { Users.username eq user.username }.toList()
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