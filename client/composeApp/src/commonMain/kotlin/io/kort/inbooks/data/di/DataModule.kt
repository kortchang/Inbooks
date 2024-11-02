package io.kort.inbooks.data.di

import io.kort.inbooks.domain.repository.CollectedBookRepository
import io.kort.inbooks.data.repository.DefaultCollectedBookRepository
import io.kort.inbooks.data.repository.DefaultGetBookByUrlRepository
import io.kort.inbooks.data.repository.DefaultSearchBookRepository
import io.kort.inbooks.data.repository.DefaultTopicRepository
import io.kort.inbooks.data.source.AppDatabase
import io.kort.inbooks.data.source.book.BookLocalDataSource
import io.kort.inbooks.data.source.collectedbook.CollectedBookLocalDateSource
import io.kort.inbooks.data.source.createAppDataStore
import io.kort.inbooks.data.source.seachbook.SearchBookCombinedRemoteDataSource
import io.kort.inbooks.data.source.seachbook.google.BookGoogleRemoteDataSource
import io.kort.inbooks.data.source.seachbook.SearchBookRemoteDataSource
import io.kort.inbooks.data.source.settings.SettingsLocalDataSource
import io.kort.inbooks.data.source.topic.TopicLocalDataSource
import io.kort.inbooks.domain.repository.GetBookByUrlRepository
import io.kort.inbooks.domain.repository.SearchBookRepository
import io.kort.inbooks.domain.repository.SettingsRepository
import io.kort.inbooks.domain.repository.TopicRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun dataModule() = module {
    factory<CollectedBookLocalDateSource> { get<AppDatabase>().collectedBookDao() }
    factory<BookLocalDataSource> { get<AppDatabase>().bookDao() }
    factory<TopicLocalDataSource> { get<AppDatabase>().topicDao() }

    singleOf<BookGoogleRemoteDataSource>(::BookGoogleRemoteDataSource)
    singleOf(::SearchBookCombinedRemoteDataSource) bind SearchBookRemoteDataSource::class
    factoryOf(::DefaultSearchBookRepository) bind SearchBookRepository::class
    factoryOf(::DefaultCollectedBookRepository) bind CollectedBookRepository::class
    factoryOf(::DefaultGetBookByUrlRepository) bind GetBookByUrlRepository::class
    factoryOf(::DefaultTopicRepository) bind TopicRepository::class

    single<SettingsRepository> { SettingsLocalDataSource(dataStore = createAppDataStore("settings")) }

}