package io.kort.inbooks.data.source

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

object AppDatabaseMigration {
    fun getAll(): List<Migration> = listOf(from1to2())

    private fun from1to2() = object : Migration(1, 2) {
        override fun migrate(connection: SQLiteConnection) {
            connection.execSQL(
                "CREATE INDEX IF NOT EXISTS `index_book_external_ids_book_id` ON `book_external_ids` (`book_id`)"
            )
        }
    }
}