package io.kort.inbooks

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.sqlite.execSQL
import io.kort.inbooks.data.source.AppDatabase
import io.kort.inbooks.data.source.commonConfiguration

fun getAppDatabase(context: Context): AppDatabase {
    val databaseFile = context.getDatabasePath("app.db")
    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = databaseFile.absolutePath
    ).commonConfiguration()
        .setDriver(AndroidSQLiteDriver())
        .build()
}