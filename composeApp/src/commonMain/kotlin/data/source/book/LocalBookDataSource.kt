package data.source.book

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import domain.book.Book

@Dao
interface LocalBookDataSource {
    /**
     * @return true if the book is inserted, false if the book is already in the database
     */
    @Transaction
    suspend fun insert(book: BookLocalModel): Boolean {
        if (book.hasSameBook()) return false

        val bookId = book.basic.id
        insertBook(book.basic)
        insertBookAuthors(book.authors)
        insertBookAuthorsCrossReference(book.authors.map { author ->
            BookLocalModel.BookAuthorCrossReferenceLocalModel(bookId, author.name)
        })
        insertBookCategories(book.categories)
        insertBookCategoriesCrossReference(book.categories.map { category ->
            BookLocalModel.BookCategoryCrossReferenceLocalModel(bookId, category.name)
        })

        return true
    }

    suspend fun BookLocalModel.hasSameBook() =
        getSameBook(basic.idBySource, basic.source, basic.isbn13) != null

    @Query(
        """
        SELECT * FROM books
        WHERE (id_by_source == :idBySource AND source == :source) OR (:isbn13 IS NOT NULL AND isbn_13 == :isbn13)
        LIMIT 1
        """
    )
    suspend fun getSameBook(
        idBySource: String,
        source: Book.Source,
        isbn13: String?
    ): BookLocalModel.BasicBookLocalModel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: BookLocalModel.BasicBookLocalModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookAuthors(authors: List<BookLocalModel.AuthorLocalModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookAuthorsCrossReference(crossReferences: List<BookLocalModel.BookAuthorCrossReferenceLocalModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookCategories(categories: List<BookLocalModel.CategoryLocalModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookCategoriesCrossReference(crossReferences: List<BookLocalModel.BookCategoryCrossReferenceLocalModel>)
}