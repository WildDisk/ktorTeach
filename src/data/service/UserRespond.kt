package ru.wilddisk.data.service

import data.model.UserRegistering
import data.service.User

class UserRespond(private val userRegistering: UserRegistering): User by userRegistering {
    override fun find(): UserRegistering = if(this.userRegistering.find().id > -1) this.userRegistering.find()
    else throw RuntimeException("User not found")
}