package ru.wilddisk.auth

import data.model.User
import ru.wilddisk.data.repository.UserByName

class LoginUser(private val user: User) : Auth by user {
    fun login(): User = user.let {
        val authorizedUser: User = UserByName(user).find() ?: throw UserNotFound
        BcryptHash.checkPassword(user.password, authorizedUser)
        authorizedUser
    }
}