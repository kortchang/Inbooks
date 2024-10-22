package io.kort.inbooks.data.source.collectedbook

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import io.kort.inbooks.data.source.book.BookLocalModel
import io.kort.inbooks.domain.model.Editable
import kotlinx.datetime.Instant

data class CollectedBookLocalModel(
    @Embedded val basic: BasicCollectedBookLocalModel,
    @Relation(
        entity = BookLocalModel.BasicBookLocalModel::class,
        parentColumn = "book_id",
        entityColumn = "id",
    )
    val book: BookLocalModel,
    @Relation(
        parentColumn = "book_id",
        entityColumn = "book_id",
    )
    val readingEvents: List<ReadingEventLocalModel> = emptyList(),
) {
    @Entity(
        tableName = "collected_books",
        foreignKeys = [
            ForeignKey(
                entity = BookLocalModel.BasicBookLocalModel::class,
                parentColumns = ["id"],
                childColumns = ["book_id"],
                onDelete = ForeignKey.CASCADE,
            )
        ]
    )
    data class BasicCollectedBookLocalModel(
        @PrimaryKey @ColumnInfo("book_id") val bookId: String,
        @ColumnInfo("reading_reason") val readingReason: String,
        @ColumnInfo("modified_at") override val modifiedAt: Instant?,
        @ColumnInfo("created_at") override val createdAt: Instant,
    ) : Editable

    @Entity(
        tableName = "book_reading_events",
        foreignKeys = [
            ForeignKey(
                entity = BookLocalModel.BasicBookLocalModel::class,
                parentColumns = ["id"],
                childColumns = ["book_id"],
                onDelete = ForeignKey.CASCADE,
            )
        ]
    )
    data class ReadingEventLocalModel(
        @PrimaryKey @ColumnInfo("id") val id: String,
        @ColumnInfo("book_id", index = true) val bookId: String,
        @ColumnInfo("at") val at: Instant,
        @ColumnInfo("page") val page: Int,
        @ColumnInfo("modified_at") override val modifiedAt: Instant?,
        @ColumnInfo("created_at") override val createdAt: Instant,
    ) : Editable
}