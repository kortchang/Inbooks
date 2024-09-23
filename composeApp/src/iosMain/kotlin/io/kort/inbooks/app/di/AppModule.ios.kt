package io.kort.inbooks.app.di

import io.kort.inbooks.data.source.AppDatabase
import io.kort.inbooks.data.source.getAppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<AppDatabase> { getAppDatabase() }
}