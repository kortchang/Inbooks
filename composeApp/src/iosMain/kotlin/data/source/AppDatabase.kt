package data.source

import androidx.room.Room
import platform.Foundation.NSHomeDirectory

fun getAppDatabase(): AppDatabase {
    val databaseFilePath = NSHomeDirectory() + "/app.db"
    return Room.databaseBuilder<AppDatabase>(
        name = databaseFilePath,
        factory = { AppDatabase::class.instantiateImpl() }
    ).commonConfiguration().build()
}