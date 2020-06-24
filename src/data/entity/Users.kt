package data.entity

import org.jetbrains.exposed.sql.Table

object Users : Table() {
    val id = long("id").primaryKey().autoIncrement()
    val username = varchar("username", 255)
    val password = varchar("password", 255)
    val firstName = varchar("first_name", 255)
    val lastName = varchar("last_name", 255)
    val email = varchar("email", 255)
}