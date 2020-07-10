package ru.wilddisk.data.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import data.model.UserRegistering
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import ru.wilddisk.data.entity.tables
import ru.wilddisk.model.Role

object ConnectionDatabase {
    init {
        val config = HikariConfig("/hikari.properties")
        config.schema = "public"
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
        try {
            UserRegistering(1).find()
        } catch (e: Exception) {
            transaction { SchemaUtils.create(*tables) }
            UserRegistering(-1,"admin", "admin", "", "", "admin@example.com", setOf(Role.ADMIN, Role.USER)).save()
        }
    }
}