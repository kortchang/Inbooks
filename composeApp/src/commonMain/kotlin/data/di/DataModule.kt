package data.di

import data.repository.DefaultSearchBookRepository
import data.repository.SearchBookRepository
import data.source.searchbook.GoogleRemoteSearchBooksDataSource
import data.source.searchbook.RemoteSearchBookDataSource
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun dataModule() = module {
    singleOf<RemoteSearchBookDataSource>(::GoogleRemoteSearchBooksDataSource)
    factoryOf(::DefaultSearchBookRepository) bind SearchBookRepository::class
}