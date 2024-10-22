package io.kort.inbooks.data.migration

import io.kort.inbooks.data.DatabaseConfiguration
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.transactions.transaction

interface MigrationManager {
    fun migrate()
}

class FlywayMigrationManager(private val databaseConfiguration: DatabaseConfiguration) : MigrationManager {
    override fun migrate() {
        transaction {
            Flyway
                .configure()
                .dataSource(
                    databaseConfiguration.url,
                    databaseConfiguration.user,
                    databaseConfiguration.password
                )
                .locations("migrations")
                .load()
                .migrate()
        }
    }
}