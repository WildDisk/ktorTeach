package ru.wilddisk.data.service

import data.model.UserRegistering
import data.service.User
import ru.wilddisk.auth.*

class UserRespond(private val userRegistering: UserRegistering): User by userRegistering {
    override fun find(): UserRegistering = if(this.userRegistering.find().id > -1) this.userRegistering.find()
    else throw UserNotFound
    override fun save() = when {
        userRegistering.username == UserRegistering(username = userRegistering.username).find().username &&
                userRegistering.username != "" -> throw UserWithUsernameAlreadyExists
        userRegistering.email == UserRegistering(email = userRegistering.email).find().email -> throw UserWithEmailAlreadyExists
        userRegistering.username.isEmpty() -> throw UsernameMustNoBeEmpty
        userRegistering.password.isEmpty() -> throw PasswordMustNoBeEmpty
        userRegistering.email.isEmpty() -> throw EmailMustNoBeEmpty
        else -> userRegistering.save()
    }
}