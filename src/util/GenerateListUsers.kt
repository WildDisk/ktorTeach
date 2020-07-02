package ru.wilddisk.util

import ru.wilddisk.data.repository.UserRepository

/**
 * Generate first users
 */
val users = UserRepository().allUsers().toMutableList()