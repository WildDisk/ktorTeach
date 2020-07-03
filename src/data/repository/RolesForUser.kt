package ru.wilddisk.data.repository

import data.entity.Users
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.wilddisk.migration.Roles
import ru.wilddisk.model.Role

class RolesForUser(private val userId: Long) {
    fun roles(): Set<Role> {
        val list = mutableSetOf<Role>()
        transaction {
            (Users leftJoin Roles)
                .select { Users.id eq Roles.userId }
                .andWhere { Users.id eq userId }.toList()
        }.forEach {
            list.add(Role.valueOf(it[Roles.role]))
        }
        return list
    }
}