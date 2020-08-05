package ru.wilddisk.data.service

import data.model.Role
import data.model.UserRegistering
import ru.wilddisk.auth.AccessDenied
import ru.wilddisk.auth.MessageIsEmpty
import ru.wilddisk.auth.MessageNotFound
import ru.wilddisk.auth.WrongMessageId
import ru.wilddisk.data.model.MessageStatus
import ru.wilddisk.data.model.UserMessage

class MessageRespond(
    private val userRegistering: UserRegistering,
    private val userMessage: UserMessage
) : Message {
    override fun save() = when (userMessage.message) {
        "" -> throw MessageIsEmpty
        else -> UserMessage(message = userMessage.message, userId = userRegistering.id).save()
    }

    override fun find(): UserMessage = if (userMessage.messageId > -1 && UserMessage(
            messageId = userMessage.messageId,
            userId = userRegistering.id
        ).find().userId == userRegistering.id
    ) userMessage.find()
    else throw MessageNotFound

    override fun update() = when {
        !userRegistering.role.contains(Role.ADMIN) -> throw AccessDenied
        userMessage.find().messageId < 0 -> throw WrongMessageId
        else -> UserMessage(
            messageId = userMessage.messageId,
            message = if (userMessage.message.isEmpty()) UserMessage(messageId = userMessage.messageId).find().message
            else userMessage.message,
            messageStatus = when (userMessage.messageStatus) {
                "CLOSED" -> MessageStatus.CLOSED.toString()
                "OPEN" -> MessageStatus.OPEN.toString()
                else -> MessageStatus.OPEN.toString()
            }
        ).update()
    }
}