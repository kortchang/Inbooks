package io.kort.inbooks.data

import io.kort.inbooks.data.migration.MigrationManager
import io.ktor.server.application.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.get

fun Application.configureDatabases() {
    val configuration = get<DatabaseConfiguration>()
    val database = Database.connect(
        url = configuration.url,
        user = configuration.user,
        password = configuration.password
    )
    TransactionManager.defaultDatabase = database
    get<MigrationManager>().migrate()
}