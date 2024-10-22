package io.kort.inbooks.data

import io.kort.inbooks.data.migration.FlywayMigrationManager
import io.kort.inbooks.data.migration.MigrationManager
import io.kort.inbooks.data.repository.DefaultRemoteUserRepository
import org.flywaydb.core.Flyway
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import service.RemoteUserRepository
import kotlin.coroutines.CoroutineContext

val DatabaseModules = module {
    single<DatabaseConfiguration> { EnvironmentDatabaseConfiguration(get()) }
    single<RemoteUserRepository> { (coroutineContext: CoroutineContext) ->
        DefaultRemoteUserRepository(coroutineContext, get(), get(), get(), get())
    }
    singleOf(::FlywayMigrationManager) bind MigrationManager::class
}