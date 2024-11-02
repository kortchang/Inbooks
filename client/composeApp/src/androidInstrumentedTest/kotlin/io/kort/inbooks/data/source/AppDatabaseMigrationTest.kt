package io.kort.inbooks.data.source

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.SQLiteDriver
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule

class AppDatabaseMigrationTest : BaseAppDatabaseMigrationTest() {
    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val file = instrumentation.targetContext.getDatabasePath("test.db")
    private val driver: SQLiteDriver = BundledSQLiteDriver()

    @get:Rule
    private val testHelper = MigrationTestHelper(
        instrumentation = instrumentation,
        file = file,
        driver = driver,
        databaseClass = AppDatabase::class
    )

    override fun getMigrationTestHelper(): MigrationTestHelper {
        return testHelper
    }
}