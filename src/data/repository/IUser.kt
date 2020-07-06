package ru.wilddisk.data.repository

import data.model.User

interface IUser {
    fun find(): User?
    fun save()
    fun update()
}