package io.kort.inbooks.domain.usecase

import io.kort.inbooks.domain.repository.CollectedBookRepository
import io.kort.inbooks.domain.model.book.SearchedBook
import kotlinx.coroutines.flow.first

/**
 * 確認搜尋到的書籍是否是已經收藏過的了。
 */
class ResolveSearchedBooksToCollectedOrNotUseCase(
    private val collectedBookRepository: CollectedBookRepository,
) {
    /**
     * @return List<SearchedBook | CollectedBook>
     */
    suspend operator fun invoke(searchedBooks: List<SearchedBook>): List<Any> {
       return searchedBooks.map { searchedBook ->
           /**
            * 如果有已經被收藏的書籍，就把 SearchedBook 轉換成 CollectedBook，
            * 讓外面的人可以判斷接下來的行為（例如顯示或是跳轉）。
            */
           collectedBookRepository
               .get(searchedBook.book.id)
               .first()
               ?: searchedBook
       }
   }
}