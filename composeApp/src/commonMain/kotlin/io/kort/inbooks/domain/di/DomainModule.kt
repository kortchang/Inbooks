package io.kort.inbooks.domain.di

import io.kort.inbooks.domain.usecase.ResolveSearchedBooksToCollectedOrNotUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun domainModule() = module {
    factoryOf(::ResolveSearchedBooksToCollectedOrNotUseCase)
}