package io.kort.inbooks.data.migration

import MigrationUtils
import io.kort.inbooks.data.DatabaseConfiguration
import io.kort.inbooks.data.model.Authentications
import io.kort.inbooks.data.model.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ExperimentalDatabaseMigrationApi
import org.jetbrains.exposed.sql.transactions.transaction

@OptIn(ExperimentalDatabaseMigrationApi::class)
fun main() {
    val databaseConfiguration = object : DatabaseConfiguration {
        override val url = System.getenv("DATABASE_URL")
        override val user = System.getenv("DATABASE_USER")
        override val password = System.getenv("DATABASE_PASSWORD")
    }

    val database = Database.connect(
        url = databaseConfiguration.url,
        user = databaseConfiguration.user,
        password = databaseConfiguration.password
    )

    val scriptDirectory = "server/src/main/resources/migrations"

    transaction(database) {
        MigrationUtils.generateMigrationScript(
            Users, Authentications,
            scriptDirectory = scriptDirectory,
            scriptName = "V1__Initial"
        )
    }
}