package ru.wilddisk.data.service

import ru.wilddisk.data.model.UserMessage

interface Message {
    fun save()
    fun find(): UserMessage
    fun update()
}