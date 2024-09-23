package io.kort.inbooks.data.source.topic

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import io.kort.inbooks.data.source.book.BookLocalModel
import io.kort.inbooks.domain.model.Editable
import kotlinx.datetime.Instant

data class TopicLocalModel(
    @Embedded
    val topic: BasicTopicLocalModel,
    @Relation(
        entity = BasicTopicLocalModel.TopicBookCrossReferenceLocalModel::class,
        parentColumn = "id",
        entityColumn = "topic_id",
    )
    val books: List<BasicTopicLocalModel.TopicBookLocalModel>,
)

@Entity(tableName = "topics")
data class BasicTopicLocalModel(
    @PrimaryKey @ColumnInfo("id") val id: String,
    @ColumnInfo("created_at") override val createdAt: Instant,
    @ColumnInfo("modified_at") override val modifiedAt: Instant?,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("description") val description: String,
) : Editable {
    @Entity(
        tableName = "topic_book_cross_reference",
        primaryKeys = ["topic_id", "book_id"],
        foreignKeys = [
            ForeignKey(
                entity = BasicTopicLocalModel::class,
                parentColumns = ["id"],
                childColumns = ["topic_id"],
                onDelete = ForeignKey.CASCADE,
            ),
            ForeignKey(
                entity = BookLocalModel.BasicBookLocalModel::class,
                parentColumns = ["id"],
                childColumns = ["book_id"],
                onDelete = ForeignKey.CASCADE,
            ),
        ]
    )
    data class TopicBookCrossReferenceLocalModel(
        @ColumnInfo("topic_id") val topicId: String,
        @ColumnInfo("book_id") val bookId: String,
        @ColumnInfo("created_at") val createdAt: Instant,
    )

    data class TopicBookLocalModel(
        @Embedded
        val reference: TopicBookCrossReferenceLocalModel,
        @Relation(
            entity = BookLocalModel.BasicBookLocalModel::class,
            parentColumn = "book_id",
            entityColumn = "id",
        )
        val book: BookLocalModel,
    )
}