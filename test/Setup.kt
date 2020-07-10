package ru.wilddisk

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

object Setup {
    init {
        val config = HikariConfig("/hikari-test.properties")
        config.schema = "public"
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
    }
}