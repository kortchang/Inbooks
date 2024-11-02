package io.kort.inbooks.data

import io.kort.inbooks.data.migration.FlywayMigrationManager
import io.kort.inbooks.data.migration.MigrationManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import io.kort.inbooks.data.repository.UserRepository

val DatabaseModules = module {
    single<DatabaseConfiguration> { EnvironmentDatabaseConfiguration(get()) }
    singleOf(::UserRepository)
    singleOf(::FlywayMigrationManager) bind MigrationManager::class
}