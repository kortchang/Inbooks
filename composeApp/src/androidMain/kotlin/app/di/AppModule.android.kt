package app.di

import data.source.AppDatabase
import io.kort.book.getAppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule(): Module = module {
    single<AppDatabase> {
        val application = androidApplication()
        getAppDatabase(application)
    }
}