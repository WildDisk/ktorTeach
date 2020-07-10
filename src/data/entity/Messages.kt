package data.entity

import org.jetbrains.exposed.sql.Table

object Messages : Table() {
    val messageId = long("message_id").primaryKey().autoIncrement()
    val message = text("message")
    val userId = long("user_id").references(Users.id)
    val messageStatus = varchar("message_status", 10)
}