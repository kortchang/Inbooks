package app.di

import data.di.dataModule
import ui.di.uiModule

fun appModules() = listOf(
    dataModule(), uiModule()
)