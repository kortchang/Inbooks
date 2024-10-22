package io.kort.inbooks

import io.kort.inbooks.data.configureDatabases
import io.kort.inbooks.plugin.configureDI
import io.kort.inbooks.plugin.configureSecurity
import io.kort.inbooks.plugin.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    configureDI()
    configureSecurity()
    configureSerialization()
    configureDatabases()
}