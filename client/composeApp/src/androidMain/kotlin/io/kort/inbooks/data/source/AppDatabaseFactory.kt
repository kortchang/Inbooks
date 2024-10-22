package io.kort.inbooks.data.source

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import io.kort.inbooks.common.BuildType
import io.kort.inbooks.common.getBuildType

fun getAppDatabase(context: Context): AppDatabase {
    val databaseFile = context.getDatabasePath("app.db")
    return Room.databaseBuilder<AppDatabase>(
        context = context,
        name = databaseFile.absolutePath
    )
        .commonConfiguration()
        // 這樣材可以用 inspector 來看資料庫
        .run { if (getBuildType() == BuildType.Debug) setDriver(AndroidSQLiteDriver()) else this }
        .build()
}