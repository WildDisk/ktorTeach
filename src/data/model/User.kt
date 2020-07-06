package data.model

import io.ktor.auth.Principal
import ru.wilddisk.auth.Auth
import ru.wilddisk.data.repository.IUser
import ru.wilddisk.model.Role

data class User(
    val id: Long? = null,
    val username: String,
    val password: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val role: Set<Role> = setOf(Role.USER)
) : Principal, IUser, Auth {
    constructor() : this(
        id = null,
        username = "",
        password = "",
        firstName = null,
        lastName = null,
        email = ""
    )
    constructor(username: String, password: String) : this(
        id = null,
        username = username,
        password = password
    )
    constructor(
        username: String,
        password: String,
        firstName: String?,
        lastName: String?,
        email: String?
    ) : this(
        id = null,
        username = username,
        password = password,
        firstName = firstName,
        lastName = lastName,
        email = email
    )

    override fun find(): User? = null

    override fun save() {}

    override fun update() {}
}