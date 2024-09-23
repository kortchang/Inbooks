package io.kort.inbooks

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import androidx.core.database.sqlite.transaction
import androidx.room.execSQL
import androidx.room.useWriterConnection
import androidx.sqlite.driver.AndroidSQLiteConnection
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.sqlite.execSQL
import co.touchlab.kermit.Logger
import io.kort.inbooks.app.di.startKoin
import io.kort.inbooks.data.source.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
        }
    }
}