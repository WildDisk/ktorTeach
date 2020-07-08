package ru.wilddisk.data.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import data.model.User
import io.ktor.application.Application
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import ru.wilddisk.data.entity.tables
import ru.wilddisk.data.repository.UserById
import ru.wilddisk.data.repository.UserSave
import ru.wilddisk.model.Role

fun Application.connectionDatabase() {
    val config = HikariConfig("/hikari.properties")
    config.schema = "public"
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)
    try {
        UserById(User(1)).find()
    } catch (e: Exception) {
        transaction { SchemaUtils.create(*tables) }
        UserSave(User(null, "admin", "admin", "", "", "admin@example.com", setOf(Role.ADMIN, Role.USER))).save()
    }
}