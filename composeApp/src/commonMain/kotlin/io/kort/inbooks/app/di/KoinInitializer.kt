package io.kort.inbooks.app.di

import co.touchlab.kermit.Logger
import co.touchlab.kermit.koin.KermitKoinLogger
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun startKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        modules(appModules())
        logger(
            KermitKoinLogger(Logger.withTag("[Koin]"))
        )
        appDeclaration()
    }
}