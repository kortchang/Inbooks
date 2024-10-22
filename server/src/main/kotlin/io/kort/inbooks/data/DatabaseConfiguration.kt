package io.kort.inbooks.data

import io.ktor.server.application.ApplicationEnvironment

interface DatabaseConfiguration {
    val url: String
    val user: String
    val password: String
}

class EnvironmentDatabaseConfiguration(environment: ApplicationEnvironment) : DatabaseConfiguration {
    override val url = environment.config.property("database.url").getString()
    override val user = environment.config.property("database.user").getString()
    override val password = environment.config.property("database.password").getString()
}