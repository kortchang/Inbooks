package io.kort.inbooks.plugin

import io.kort.inbooks.routing.user
import io.ktor.server.application.Application
import org.koin.ktor.ext.get

fun Application.configureRouting() {
    user()
}