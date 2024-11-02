package io.kort.inbooks.common.service.book

import io.kort.inbooks.common.model.book.BookRemoteModel

interface BookUrlAnalyzer {
    suspend fun canAnalyze(url: String): Boolean
    suspend fun analyze(url: String): BookRemoteModel?
}