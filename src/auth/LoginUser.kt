package ru.wilddisk.auth

import data.model.User
import ru.wilddisk.data.repository.UserByName

class LoginUser(private val user: User) : Auth by user {
    fun login(): User = user.let {
        UserByName(user).find() ?: throw UserNotFound
    }
}