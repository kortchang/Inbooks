package io.kort.inbooks.data.source

import androidx.annotation.VisibleForTesting
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

object AppDatabaseMigration {
    const val version = 3
    fun getAll(): List<Migration> = listOf(from1to2(), from2to3())

    private fun from1to2() = object : Migration(1, 2) {
        override fun migrate(connection: SQLiteConnection) {
            connection.execSQL(
                "CREATE INDEX IF NOT EXISTS `index_book_external_ids_book_id` ON `book_external_ids` (`book_id`)"
            )
        }
    }

    private fun from2to3() = object : Migration(2, 3) {
        override fun migrate(connection: SQLiteConnection) {
            //language=SQLite
            connection.execSQL(
                "ALTER TABLE books ADD COLUMN source TEXT NOT NULL DEFAULT 'google_book_api'"
            )
        }
    }
}