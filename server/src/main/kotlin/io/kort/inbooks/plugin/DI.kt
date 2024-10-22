package io.kort.inbooks.plugin

import io.kort.inbooks.data.DatabaseModules
import io.kort.inbooks.service.ServiceModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.core.logger.Level
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE
import org.koin.ktor.plugin.Koin
import org.koin.ktor.plugin.koin

fun Application.configureDI() {
    install(Koin) {
        logger(object : Logger() {
            override fun display(level: Level, msg: MESSAGE) {
                val level = when (level) {
                    Level.DEBUG -> org.slf4j.event.Level.DEBUG
                    Level.INFO -> org.slf4j.event.Level.INFO
                    Level.WARNING -> org.slf4j.event.Level.WARN
                    Level.ERROR -> org.slf4j.event.Level.ERROR
                    Level.NONE -> org.slf4j.event.Level.TRACE
                }
                environment.log.atLevel(level).log("[Koin] $msg")
            }
        })
    }

    koin {
        val applicationModule = org.koin.dsl.module {
            single { environment }
        }

        modules(
            applicationModule,
            DatabaseModules,
            ServiceModule,
        )
    }
}