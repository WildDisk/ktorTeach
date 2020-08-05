package ru.wilddisk.data.model

import data.entity.Messages
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import ru.wilddisk.data.service.Message

data class UserMessage(
    val messageId: Long = -1,
    val message: String = "",
    val userId: Long = -1,
    val messageStatus: String = MessageStatus.OPEN.toString()
): Message {
    override fun save() {
        transaction {
            Messages.insert {
                it[message] = this@UserMessage.message
                it[userId] = this@UserMessage.userId
                it[messageStatus] = this@UserMessage.messageStatus
            }
        }
    }

    override fun find(): UserMessage {
        var usersMessage = UserMessage()
        when {
            messageId > (-1).toLong() -> transaction {
                Messages.select { Messages.messageId eq messageId }.toList()
            }
            userId > (-1).toLong() -> transaction {
                Messages.select { Messages.userId eq userId }.toList()
            }
            else -> listOf()
        }.forEach {
            usersMessage = UserMessage(
                it[Messages.messageId],
                it[Messages.message],
                it[Messages.userId],
                it[Messages.messageStatus]
            )
        }
        return usersMessage
    }

    override fun update() {
        transaction {
            Messages.update({ Messages.messageId eq this@UserMessage.messageId }) {
                it[this.message] = this@UserMessage.message
                it[messageStatus] = this@UserMessage.messageStatus
            }
        }
    }
}