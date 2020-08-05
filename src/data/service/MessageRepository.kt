package ru.wilddisk.data.service

import data.entity.Messages
import data.entity.Users
import data.model.Role
import data.model.UserRegistering
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ru.wilddisk.auth.AccessDenied

class MessageRepository(private val userRegistering: UserRegistering) {
    fun allMessages(): List<Map<String, String>> {
        val messages = mutableListOf<Map<String, String>>()
        if (userRegistering.role.contains(Role.ADMIN)) transaction {
            (Messages leftJoin Users)
                .slice(Messages.messageId, Messages.message, Users.username, Messages.messageStatus)
                .selectAll()
                .forEach { message ->
                    messages.add(
                        mapOf(
                            "messageId" to message[Messages.messageId].toString(),
                            "message" to message[Messages.message],
                            "username" to message[Users.username],
                            "messageStatus" to message[Messages.messageStatus]
                        )
                    )
                }
        } else throw AccessDenied
        return messages
    }
}