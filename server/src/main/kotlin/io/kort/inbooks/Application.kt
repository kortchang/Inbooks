package io.kort.inbooks

import io.kort.inbooks.data.configureDatabases
import io.kort.inbooks.plugin.configureAuthentication
import io.kort.inbooks.plugin.configureDI
import io.kort.inbooks.plugin.configureRouting
import io.kort.inbooks.plugin.configureSerialization
import io.kort.inbooks.plugin.configureStatusPage
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    configureDI()
    configureAuthentication()
    configureSerialization()
    configureDatabases()
    configureRouting()
    configureStatusPage()
}