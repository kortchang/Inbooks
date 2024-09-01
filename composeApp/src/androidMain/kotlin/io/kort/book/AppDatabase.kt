package io.kort.book

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabaseConstructor
import data.source.AppDatabase
import data.source.commonConfiguration

fun getAppDatabase(context: Context): AppDatabase {
    val databaseFile = context.getDatabasePath("app.db")
    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = databaseFile.absolutePath
    ).commonConfiguration().build()
}