package io.kort.inbooks.data.source

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.benasher44.uuid.Uuid
import com.benasher44.uuid.uuidFrom
import io.kort.inbooks.data.source.book.BookLocalModel
import io.kort.inbooks.data.source.book.BookLocalDataSource
import io.kort.inbooks.data.source.collectedbook.CollectedBookLocalDateSource
import io.kort.inbooks.data.source.collectedbook.CollectedBookLocalModel
import io.kort.inbooks.data.source.topic.BasicTopicLocalModel
import io.kort.inbooks.data.source.topic.TopicLocalDataSource
import io.kort.inbooks.domain.model.book.LocalYearAndMonth
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
        BookLocalModel.ExternalIdLocalModel::class,
        BookLocalModel.AuthorLocalModel::class,
        BookLocalModel.CategoryLocalModel::class,
        BookLocalModel.BookCategoryCrossReferenceLocalModel::class,
        BookLocalModel.BookAuthorCrossReferenceLocalModel::class,
        CollectedBookLocalModel.BasicCollectedBookLocalModel::class,
        CollectedBookLocalModel.ReadingEventLocalModel::class,
        BasicTopicLocalModel::class,
        BasicTopicLocalModel.TopicBookCrossReferenceLocalModel::class,
    ],
    version = 1,
)
@TypeConverters(AppTypeConverters::class)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookLocalDataSource
    abstract fun collectedBookDao(): CollectedBookLocalDateSource
    abstract fun topicDao(): TopicLocalDataSource
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>

fun RoomDatabase.Builder<AppDatabase>.commonConfiguration(): RoomDatabase.Builder<AppDatabase> {
    return this.setQueryCoroutineContext(Dispatchers.IO)
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
    fun fromExternalIdentifierType(value: BookLocalModel.ExternalIdLocalModel.Type): String = value.serialName

    @TypeConverter
    fun toExternalIdentifierType(value: String): BookLocalModel.ExternalIdLocalModel.Type =
        BookLocalModel.ExternalIdLocalModel.Type.entries.first { it.serialName == value }

    @TypeConverter
    fun fromListString(value: List<String>): String = Json.encodeToString(value)

    @TypeConverter
    fun toListString(value: String): List<String> = Json.decodeFromString(value)

    @TypeConverter
    fun fromLocalDate(value: LocalDate): String = value.format(LocalDate.Formats.ISO)

    @TypeConverter
    fun toLocalDate(value: String): LocalDate = LocalDate.parse(value)

    /**
     * 只是用來查詢資料而已，所以不寫 to。
     */
    @TypeConverter
    fun fromLocalYearAndMonth(value: String): LocalYearAndMonth {
        val (year, month) = value.split("-")
        return LocalYearAndMonth(year.toInt(), month.toInt())
    }
}