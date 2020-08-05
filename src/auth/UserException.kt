package ru.wilddisk.auth

import io.ktor.http.HttpStatusCode

open class UserException(private val status: HttpStatusCode, override val message: String) : Exception()
val UserNotFound = UserException(
    HttpStatusCode.NotFound,
    "User not found!"
)
val UserWithUsernameAlreadyExists = UserException(
    HttpStatusCode.Conflict,
    "User with that username already exists!"
)
val UserWithEmailAlreadyExists = UserException(
    HttpStatusCode.Conflict,
    "User with that email already exists!"
)
val UsernameMustNoBeEmpty = UserException(
    HttpStatusCode.BadRequest,
    "Username must not be empty!"
)
val PasswordMustNoBeEmpty = UserException(
    HttpStatusCode.BadRequest,
    "Password must not be empty!"
)
val EmailMustNoBeEmpty = UserException(
    HttpStatusCode.BadRequest,
    "Email must not be empty!"
)
val AccessDenied = UserException(
    HttpStatusCode.Conflict,
    "Access denied!"
)
