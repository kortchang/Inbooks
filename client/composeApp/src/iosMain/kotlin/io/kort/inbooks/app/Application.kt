package io.kort.inbooks.app

import io.kort.inbooks.app.di.startKoin


class Application {
    private val commonApp = CommonApp()

    fun onCreate() {
        commonApp.onCreate()
        startKoin()
    }
}