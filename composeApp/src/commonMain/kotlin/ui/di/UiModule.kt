package ui.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ui.screen.search.SearchViewModel
import ui.screen.searcheddetail.SearchedBookDetailViewModel

fun uiModule() = module {
    factoryOf(::SearchViewModel)
    factory { (bookId: String) -> SearchedBookDetailViewModel(bookId, get()) }
}