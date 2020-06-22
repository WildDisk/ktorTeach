package ru.wilddisk.util

import ru.wilddisk.model.User
import java.time.LocalDateTime

/**
 * Generate first users
 */
val users = mutableListOf(
        User("admin", "admin"),
        User("username", "password", "username@example.com", LocalDateTime.now())
)