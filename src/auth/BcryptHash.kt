package ru.wilddisk.auth

import data.model.UserRegistering
import org.mindrot.jbcrypt.BCrypt

object BcryptHash {
    fun checkPassword(attempt: String, userRegistering: UserRegistering) = if (BCrypt.checkpw(attempt, userRegistering.password)) Unit
    else throw Exception("Wrong password")
    fun hashPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())
}