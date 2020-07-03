package data.model

import io.ktor.auth.Principal
import ru.wilddisk.data.IUser
import ru.wilddisk.model.Role

open class User(
    val id: Long? = null,
    val username: String,
    val password: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String,
    val role: Set<Role> = setOf(Role.USER)
) : Principal, IUser {
    constructor() : this(
        id = null,
        username = "",
        password = "",
        firstName = null,
        lastName = null,
        email = ""
    )
    constructor(
        username: String,
        password: String,
        firstName: String?,
        lastName: String?,
        email: String
    ) : this(
        id = null,
        username = username,
        password = password,
        firstName = firstName,
        lastName = lastName,
        email = email
    )
    override fun execute() {}
}