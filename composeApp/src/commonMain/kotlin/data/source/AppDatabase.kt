package data.source

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import common.LocalDateTimeWithTimeZone
import data.source.book.BookLocalModel
import data.source.collectedbook.LocalCollectedBookDateSource
import data.source.collectedbook.model.CollectedBookLocalModel
import domain.book.Book
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Database(
    entities = [
        BookLocalModel.BasicBookLocalModel::class,
        BookLocalModel.AuthorLocalModel::class,
        BookLocalModel.CategoryLocalModel::class,
        BookLocalModel.BookCategoryCrossReferenceLocalModel::class,
        BookLocalModel.BookAuthorCrossReferenceLocalModel::class,
        CollectedBookLocalModel.BasicCollectedBookLocalModel::class,
        CollectedBookLocalModel.ReadingEventLocalModel::class,
    ],
    version = 4
)
@TypeConverters(AppTypeConverters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun collectedBookDao(): LocalCollectedBookDateSource
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor: RoomDatabaseConstructor<AppDatabase>

fun RoomDatabase.Builder<AppDatabase>.commonConfiguration(): RoomDatabase.Builder<AppDatabase> {
    return setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
}

object AppTypeConverters {
    @TypeConverter
    fun fromUuid(value: Uuid): String = value.toString()

    @TypeConverter
    fun toUuid(value: String): Uuid = uuidFrom(value)

    @TypeConverter
    fun fromInstant(value: Instant): Long = value.epochSeconds

    @TypeConverter
    fun toInstant(value: Long): Instant = Instant.fromEpochSeconds(value)

    @TypeConverter
    fun fromBookSource(value: Book.Source): String = value.serialName

    @TypeConverter
    fun toBookSource(value: String): Book.Source =
        Book.Source.entries.first { it.serialName == value }

    @TypeConverter
    fun fromListString(value: List<String>): String = Json.encodeToString(value)

    @TypeConverter
    fun toListString(value: String): List<String> = Json.decodeFromString(value)

    @TypeConverter
    fun fromLocalDate(value: LocalDate): String = value.format(LocalDate.Formats.ISO)

    @TypeConverter
    fun toLocalDate(value: String): LocalDate = LocalDate.parse(value)
}