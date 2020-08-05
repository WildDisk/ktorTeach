package ru.wilddisk.data.service

import data.model.Role
import data.model.UserRegistering
import data.service.User
import ru.wilddisk.auth.*

/**
 * decorator, performs checks before execute functions on database
 *
 * @param userRegistering authorize user
 */
class UserRespond(private val userRegistering: UserRegistering = UserRegistering()): User {
    /**
     * find user
     * if return user with id like -1, throw UserNotFound
     */
    override fun find(): UserRegistering = if(userRegistering.find().id > -1) userRegistering.find()
    else throw UserNotFound

    /**
     * validation properties before create user
     */
    override fun save() = when {
        userRegistering.username == UserRegistering(username = userRegistering.username).find().username &&
                userRegistering.username != "" -> throw UserWithUsernameAlreadyExists
        userRegistering.email == UserRegistering(email = userRegistering.email).find().email -> throw UserWithEmailAlreadyExists
        userRegistering.username.isEmpty() -> throw UsernameMustNoBeEmpty
        userRegistering.password.isEmpty() -> throw PasswordMustNoBeEmpty
        userRegistering.email.isEmpty() -> throw EmailMustNoBeEmpty
        else -> userRegistering.save()
    }

    /**
     * update user profile
     *
     * @param profile [UserRegistering] object with a new personal data
     */
    override fun update(profile: UserRegistering) {
        userRegistering.update(
            UserRegistering(
                id = userRegistering.id,
                password = if (profile.password.isEmpty()) userRegistering.password else profile.password,
                firstName = if (profile.firstName.isEmpty()) userRegistering.firstName else profile.firstName,
                lastName = if (profile.lastName.isEmpty()) userRegistering.lastName else profile.lastName,
                email = if (profile.email.isEmpty()) userRegistering.email else profile.email
            )
        )
    }

    /**
     * update users only ADMIN
     *
     * @param profile [UserRegistering] object with a new personal user authorize user
     */
    fun updateUserProfileAdmin(profile: UserRegistering) {
        if (userRegistering.role.contains(Role.ADMIN))
            if (profile.find().id > -1) userRegistering.update(
                UserRegistering(
                    id = profile.id,
                    password = if (profile.password.isEmpty()) profile.find().password else profile.password,
                    firstName = if (profile.firstName.isEmpty()) profile.find().firstName else profile.firstName,
                    lastName = if (profile.lastName.isEmpty()) profile.find().lastName else profile.lastName,
                    email = if (profile.email.isEmpty()) profile.find().email else profile.email
                )
            ) else throw UserNotFound
        else throw AccessDenied
    }

    /**
     * find user on id
     */
    fun findOnId(id: String): UserRegistering = try {
        if (UserRegistering(id = id.toLong()).find().id > -1) UserRegistering(id = id.toLong()).find() else throw UserNotFound
    } catch (e: NumberFormatException) {
        when {
            UserRegistering(username = id).find().id > -1 -> UserRegistering(username = id).find()
            UserRegistering(email = id).find().id > -1 -> UserRegistering(email = id).find()
            else -> throw UserNotFound
        }
    }
}