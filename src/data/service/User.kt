package data.service

import data.model.UserRegistering

interface User {
    fun find(): UserRegistering
    fun save()
    fun update()
}