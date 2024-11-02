package io.kort.inbooks.common.di

import io.kort.inbooks.common.service.book.BookUrlAnalyzer
import io.kort.inbooks.common.service.book.BooksUrlAnalyzer
import io.kort.inbooks.common.service.book.CombinedBookUrlAnalyzer
import org.koin.dsl.module

fun commonModule() = module {
    factory<BookUrlAnalyzer> {
        CombinedBookUrlAnalyzer(
            listOf(
                BooksUrlAnalyzer()
            )
        )
    }
}