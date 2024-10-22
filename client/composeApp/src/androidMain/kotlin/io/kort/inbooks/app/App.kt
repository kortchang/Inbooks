package io.kort.inbooks.app

import android.app.Application
import io.kort.inbooks.app.di.startKoin
import org.koin.android.ext.koin.androidContext

class App : Application() {
    private val commonApp = CommonApp()

    override fun onCreate() {
        super.onCreate()
        commonApp.onCreate()
        startKoin {
            androidContext(this@App)
        }
    }
}