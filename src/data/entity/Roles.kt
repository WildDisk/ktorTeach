package data.entity

import org.jetbrains.exposed.sql.Table

object Roles : Table() {
    val userId = long("user_id").references(Users.id)
    val role = varchar("role", 255)
}