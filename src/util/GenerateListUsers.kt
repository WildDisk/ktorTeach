package ru.wilddisk.util

import ru.wilddisk.model.Role
import data.model.User

/**
 * Generate first users
 */
val users = mutableListOf(
    User(
        "admin",
        "admin",
        null,
        null,
        null,
        mutableSetOf(Role.USER, Role.ADMIN)
    ),
    User(
        "username",
        "password",
        "Username",
        "UsernameSon",
        "username@example.com",
        mutableSetOf(Role.USER)
    )
)