package io.kort.inbooks.app.di

import io.kort.inbooks.common.di.commonModule
import io.kort.inbooks.data.di.dataModule
import io.kort.inbooks.domain.di.domainModule
import org.koin.core.module.Module
import io.kort.inbooks.ui.di.uiModule

fun appModules() = listOf(
    dataModule(),
    domainModule(),
    uiModule(),
    commonModule(),
    platformModule()
)

expect fun platformModule(): Module