package ru.wilddisk.auth

import data.model.UserRegistering

class LoginUser(private val userRegistering: UserRegistering) {
    fun login(): UserRegistering = userRegistering.let {
        val authorizedUserRegistering: UserRegistering =  if (userRegistering.find().id > -1) userRegistering.find() else throw UserNotFound
        BcryptHash.checkPassword(userRegistering.password, authorizedUserRegistering)
        authorizedUserRegistering
    }
}