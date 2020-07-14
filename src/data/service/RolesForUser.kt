package data.service

import data.entity.Roles
import data.entity.Users
import data.model.Role
import data.model.UserRegistering
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.orWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class RolesForUser(private val userRegistering: UserRegistering) {
    fun roles(): Set<Role> {
        val list = mutableSetOf<Role>()
        transaction {
            (Users leftJoin Roles)
                .select { Users.id eq Roles.userId }
                .andWhere { Users.id eq userRegistering.id }
                .orWhere { Users.username eq userRegistering.username }
                .orWhere { Users.email eq userRegistering.email }
                .toList()
        }.forEach {
            list.add(Role.valueOf(it[Roles.role]))
        }
        return list
    }
}