package ru.wilddisk.model

import io.ktor.auth.Principal
import java.time.LocalDateTime

data class User(
    val name: String,
    val password: String,
    val email: String? = null,
    val date: LocalDateTime? = null
) : Principal