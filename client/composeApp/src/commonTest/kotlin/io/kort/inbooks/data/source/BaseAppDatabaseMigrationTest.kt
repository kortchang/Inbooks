package io.kort.inbooks.data.source

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.execSQL
import androidx.sqlite.use
import io.kotest.matchers.shouldBe
import kotlin.test.Test

abstract class BaseAppDatabaseMigrationTest {
    abstract fun getMigrationTestHelper(): MigrationTestHelper

    @Test
    fun migrate_from_1_to_3() {
        val testHelper = getMigrationTestHelper()
        testHelper.createDatabase(1).apply {
            execSQL(
                """
                INSERT INTO books (id, cover_url, title, subtitle, description, published_date, publisher, page_count)
                VALUES ('1', 'https://www.books.com.tw/img/001/099/57/0010995752_bc_01.jpg', '你願意，人生就會值得：蔡康永的情商課3', null, 'description', '2024/08/01', '如何', 288);
                """
            )
        }.close()

        testHelper.runMigrationsAndValidate(
            3,
            AppDatabaseMigration.getAll().filter {
                (it.startVersion == 1 && it.endVersion == 2) ||
                        (it.startVersion == 2 && it.endVersion == 3)
            }
        ).apply {
            prepare("""SELECT * FROM books;""".trimIndent()).use {
                while (it.step()) {
                    val sourceColumnIndex = it.getColumnNames().indexOf("source")
                    it.getText(sourceColumnIndex) shouldBe "google_book_api"
                }
            }
        }
    }
}