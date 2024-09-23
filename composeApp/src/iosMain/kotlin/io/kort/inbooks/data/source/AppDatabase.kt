package io.kort.inbooks.data.source

import androidx.room.Room
import androidx.sqlite.driver.NativeSQLiteDriver
import platform.Foundation.NSHomeDirectory

fun getAppDatabase(): AppDatabase {
    val databaseFilePath = NSHomeDirectory() + "/app.db"
    return Room.databaseBuilder<AppDatabase>(name = databaseFilePath)
        .setDriver(NativeSQLiteDriver())
        .commonConfiguration().build()
}