package io.kort.inbooks.data.source

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import io.kotest.core.spec.style.FunSpec
import platform.Foundation.NSBundle
import platform.posix.remove
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class AppDatabaseMigrationTest : BaseAppDatabaseMigrationTest() {
    private val filename = "/tmp/test-${Random.nextInt()}.db"
    private lateinit var migrationTestHelper: MigrationTestHelper

    override fun getMigrationTestHelper(): MigrationTestHelper {
        return migrationTestHelper
    }

    @BeforeTest
    fun setup() {
        migrationTestHelper = MigrationTestHelper(
            schemaDirectoryPath = NSBundle.mainBundle().resourcePath + "/schemas",
            fileName = filename,
            driver = BundledSQLiteDriver(),
            databaseClass = AppDatabase::class,
        )
    }

    @AfterTest
    fun tearDown() {
        migrationTestHelper.finished()
        remove(filename)
        remove("$filename-wal")
        remove("$filename-shm")
    }
}