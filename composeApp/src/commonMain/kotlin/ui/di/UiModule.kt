package ui.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ui.feature.bookdetail.screen.collected.CollectedBookDetailViewModel
import ui.feature.dashboard.DashboardViewModel
import ui.feature.search.SearchViewModel
import ui.feature.bookdetail.screen.searched.SearchedBookDetailViewModel

fun uiModule() = module {
    factoryOf(::SearchViewModel)
    factory { (bookId: String) -> SearchedBookDetailViewModel(bookId, get(), get()) }

    factoryOf(::DashboardViewModel)
    factory { (bookId: String) -> CollectedBookDetailViewModel(bookId, get()) }
}