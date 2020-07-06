package ru.wilddisk.auth

import io.ktor.http.HttpStatusCode

class AuthorizationException : Exception() {
    val status = HttpStatusCode.Forbidden
}