package data.di

import data.repository.CollectedBookRepository
import data.repository.DefaultCollectedBookRepository
import data.repository.DefaultSearchBookRepository
import data.repository.MockUserRepository
import data.repository.SearchBookRepository
import data.repository.UserRepository
import data.source.AppDatabase
import data.source.collectedbook.LocalCollectedBookDateSource
import data.source.seachbook.GoogleBooksDataSource
import data.source.seachbook.RemoteSearchBookDataSource
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun dataModule() = module {
    factory<LocalCollectedBookDateSource> { get<AppDatabase>().collectedBookDao() }
    singleOf<RemoteSearchBookDataSource>(::GoogleBooksDataSource)
    factoryOf(::DefaultSearchBookRepository) bind SearchBookRepository::class
    factoryOf(::DefaultCollectedBookRepository) bind CollectedBookRepository::class
    factoryOf(::MockUserRepository) bind UserRepository::class
}