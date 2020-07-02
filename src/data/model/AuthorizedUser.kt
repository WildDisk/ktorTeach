package data.model

import io.ktor.auth.Principal

data class AuthorizedUser(
    val username: String,
    val password: String
): Principal