package io.kort.inbooks.data.source

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.AndroidSQLiteDriver

fun getAppDatabase(context: Context): AppDatabase {
    val databaseFile = context.getDatabasePath("app.db")
    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = databaseFile.absolutePath
    ).commonConfiguration()
        .setDriver(AndroidSQLiteDriver())
        .build()
}