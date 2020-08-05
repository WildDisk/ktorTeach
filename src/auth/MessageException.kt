package ru.wilddisk.auth

import io.ktor.http.HttpStatusCode

open class MessageException(private val status: HttpStatusCode, override val message: String) : Exception()
val MessageNotFound = MessageException(
    HttpStatusCode.NotFound,
    "Message not found!"
)
val MessageIsEmpty = MessageException(
    HttpStatusCode.BadRequest,
    "Message is empty!"
)
val WrongMessageId = MessageException(
    HttpStatusCode.BadRequest,
    "Wrong message id!"
)