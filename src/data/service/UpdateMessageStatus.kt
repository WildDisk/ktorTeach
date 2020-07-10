package ru.wilddisk.data.service

import data.entity.Messages
import data.model.UserRegistering
import data.service.User
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.wilddisk.data.model.Message

class UpdateMessageStatus(
    private val userRegistering: UserRegistering = UserRegistering(),
    private val updateMessage: Message
): User by userRegistering {
    override fun update() {
        transaction {
            Messages.update({ Messages.messageId eq (updateMessage.messageId ?: -1) }) {
                it[messageStatus] = updateMessage.messageStatus
            }
        }
    }
}