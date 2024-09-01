package app.di

import data.source.AppDatabase
import data.source.getAppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<AppDatabase> { getAppDatabase() }
}