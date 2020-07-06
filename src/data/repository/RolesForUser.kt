package ru.wilddisk.data.repository

import data.entity.Users
import data.model.User
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.orWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.wilddisk.migration.Roles
import ru.wilddisk.model.Role

class RolesForUser(private val user: User) {
    fun roles(): Set<Role> {
        val list = mutableSetOf<Role>()
        transaction {
            (Users leftJoin Roles)
                .select { Users.id eq Roles.userId }
                .andWhere { Users.id eq (user.id ?: -1) }
                .orWhere { Users.username eq user.username }
                .toList()
        }.forEach {
            list.add(Role.valueOf(it[Roles.role]))
        }
        return list
    }
}