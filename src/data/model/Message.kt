package ru.wilddisk.data.model

data class Message(
    val messageId: Long? = null,
    val message: String? = null,
    val userId: Long? = null,
    val messageStatus: String = MessageStatus.OPEN.toString()
)