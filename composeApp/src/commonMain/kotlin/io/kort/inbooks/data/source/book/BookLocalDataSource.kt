package io.kort.inbooks.data.source.book

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.kort.inbooks.domain.model.book.BookId

@Dao
interface BookLocalDataSource {
    /**
     * @return true if the book is inserted, false if the book is already in the database
     */
    @Transaction
    suspend fun insert(book: BookLocalModel): BookId {
        val existBookId = getBookIdByExternalIds(book.externalIds.map { it.type to it.value })
        if (existBookId != null) {
            /**
             * 插入可能原本書籍沒有的 identifier
             */
            upsertExternalIds(book.externalIds.map { it.copy(bookId = existBookId) })
            return existBookId
        }

        val bookId = book.basic.id
        upsertBook(book.basic)
        upsertBookAuthors(book.authors)
        upsertBookAuthorsCrossReference(book.authors.map { author ->
            BookLocalModel.BookAuthorCrossReferenceLocalModel(bookId, author.name)
        })
        upsertBookCategories(book.categories)
        upsertBookCategoriesCrossReference(book.categories.map { category ->
            BookLocalModel.BookCategoryCrossReferenceLocalModel(bookId, category.name)
        })
        upsertExternalIds(book.externalIds)

        return bookId
    }

    @Transaction
    suspend fun getBookIdByExternalIds(
        externalIds: List<Pair<BookLocalModel.ExternalIdLocalModel.Type, String>>
    ): BookId? {
        return externalIds.firstNotNullOfOrNull { externalId ->
            getBookIdByExternalId(externalId)
        }
    }

    @Transaction
    suspend fun getBookIdByExternalId(
        externalId: Pair<BookLocalModel.ExternalIdLocalModel.Type, String>,
    ): BookId? {
        return getBookIdByExternalId(externalId.first, externalId.second)
    }

    @Query(
        """
        SELECT book_id FROM book_external_ids
        WHERE type = :externalIdType AND value = :externalIdValue
        LIMIT 1
        """
    )
    suspend fun getBookIdByExternalId(
        externalIdType: BookLocalModel.ExternalIdLocalModel.Type,
        externalIdValue: String,
    ): BookId?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBook(book: BookLocalModel.BasicBookLocalModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBookAuthors(authors: List<BookLocalModel.AuthorLocalModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBookAuthorsCrossReference(crossReferences: List<BookLocalModel.BookAuthorCrossReferenceLocalModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBookCategories(categories: List<BookLocalModel.CategoryLocalModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBookCategoriesCrossReference(crossReferences: List<BookLocalModel.BookCategoryCrossReferenceLocalModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertExternalIds(ids: List<BookLocalModel.ExternalIdLocalModel>)
}