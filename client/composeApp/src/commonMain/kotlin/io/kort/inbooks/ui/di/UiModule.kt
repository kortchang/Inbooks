package io.kort.inbooks.ui.di

import io.kort.inbooks.domain.model.book.Book
import io.kort.inbooks.domain.model.topic.TopicBook
import io.kort.inbooks.ui.screen.app.AppViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import io.kort.inbooks.ui.screen.book.detail.screen.collected.CollectedBookDetailViewModel
import io.kort.inbooks.ui.screen.dashboard.DashboardViewModel
import io.kort.inbooks.ui.screen.search.SearchViewModel
import io.kort.inbooks.ui.screen.book.detail.screen.searched.SearchedBookDetailViewModel
import io.kort.inbooks.ui.screen.book.list.BookListViewModel
import io.kort.inbooks.ui.screen.onboarding.OnboardingViewModel
import io.kort.inbooks.ui.screen.search.addbylink.AddByLinkViewModel
import io.kort.inbooks.ui.screen.topic.detail.TopicDetailViewModel
import io.kort.inbooks.ui.screen.topic.edit.book.SelectBookViewModel
import io.kort.inbooks.ui.screen.topic.edit.main.AddOrEditTopicViewModel
import io.kort.inbooks.ui.screen.topic.list.TopicListViewModel

fun uiModule() = module {
    factoryOf(::AppViewModel)
    factoryOf(::OnboardingViewModel)
    factoryOf(::SearchViewModel)
    factoryOf(::AddByLinkViewModel)
    factory { (externalIds: List<Book.ExternalId>) -> SearchedBookDetailViewModel(externalIds, get(), get()) }
    factoryOf(::DashboardViewModel)
    factory { (bookId: String) -> CollectedBookDetailViewModel(bookId, get()) }
    factoryOf(::BookListViewModel)
    factory { (topicIdForEdit: String?) -> AddOrEditTopicViewModel(topicIdForEdit, get(), get()) }
    factory { (books: List<TopicBook>) -> SelectBookViewModel(books, get()) }
    factoryOf(::TopicListViewModel)
    factory { (topicId: String) -> TopicDetailViewModel(topicId, get(), get()) }
}