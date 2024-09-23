package io.kort.inbooks.data.source.book

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

data class BookLocalModel(
    @Embedded val basic: BasicBookLocalModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "book_id",
    )
    val externalIds: List<ExternalIdLocalModel> = emptyList(),
    @Relation(
        parentColumn = "id",
        entityColumn = "name",
        associateBy = Junction(
            value = BookAuthorCrossReferenceLocalModel::class,
            parentColumn = "book_id",
            entityColumn = "author_name"
        )
    )
    val authors: List<AuthorLocalModel> = emptyList(),
    @Relation(
        parentColumn = "id",
        entityColumn = "name",
        associateBy = Junction(
            value = BookCategoryCrossReferenceLocalModel::class,
            parentColumn = "book_id",
            entityColumn = "category_name"
        )
    )
    val categories: List<CategoryLocalModel> = emptyList(),
) {
    @Entity(tableName = "books")
    data class BasicBookLocalModel(
        @PrimaryKey @ColumnInfo("id") val id: String,
        @ColumnInfo("cover_url") val coverUrl: String?,
        @ColumnInfo("title") val title: String,
        @ColumnInfo("subtitle") val subtitle: String?,
        @ColumnInfo("description") val description: String?,
        @ColumnInfo("published_date") val publishedDate: String?,
        @ColumnInfo("publisher") val publisher: String?,
        @ColumnInfo("page_count") val pageCount: Int?,
    )

    @Entity(
        tableName = "book_external_ids",
        primaryKeys = ["type", "value"],
        foreignKeys = [
            ForeignKey(
                entity = BasicBookLocalModel::class,
                parentColumns = ["id"],
                childColumns = ["book_id"],
                onDelete = ForeignKey.CASCADE,
            )
        ]
    )
    data class ExternalIdLocalModel(
        @ColumnInfo("book_id") val bookId: String,
        @ColumnInfo("type") val type: Type,
        @ColumnInfo("value") val value: String,
    ) {
        enum class Type(val serialName: String) {
            ISBN13("isbn_13"),
            GoogleBookId("google_book_id"),
        }
    }

    @Entity(tableName = "book_authors")
    class AuthorLocalModel(
        @PrimaryKey @ColumnInfo("name") val name: String
    )

    @Entity(tableName = "book_authors_cross_reference", primaryKeys = ["book_id", "author_name"])
    data class BookAuthorCrossReferenceLocalModel(
        @ColumnInfo("book_id") val bookId: String,
        @ColumnInfo("author_name") val authorName: String,
    )

    @Entity(tableName = "book_categories")
    class CategoryLocalModel(
        @PrimaryKey @ColumnInfo("name") val name: String
    )

    @Entity(
        tableName = "book_categories_cross_reference",
        primaryKeys = ["book_id", "category_name"]
    )
    data class BookCategoryCrossReferenceLocalModel(
        @ColumnInfo("book_id") val bookId: String,
        @ColumnInfo("category_name") val categoryName: String,
    )
}