package ru.wilddisk.auth

import data.model.User
import org.mindrot.jbcrypt.BCrypt

object BcryptHash {
    fun checkPassword(attempt: String, user: User) = if (BCrypt.checkpw(attempt, user.password)) Unit
    else throw Exception("Wrong password")
    fun hashPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())
}