package app.di

import data.di.dataModule
import org.koin.core.module.Module
import ui.di.uiModule

fun appModules() = listOf(
    dataModule(), uiModule(), platformModule()
)

expect fun platformModule(): Module