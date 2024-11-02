package io.kort.inbooks.common.service.book

import io.kort.inbooks.common.model.book.BookRemoteModel

class CombinedBookUrlAnalyzer(
    private val analyzers: List<BookUrlAnalyzer>
) : BookUrlAnalyzer {
    override suspend fun canAnalyze(url: String): Boolean {
        return analyzers.any { it.canAnalyze(url) }
    }

    override suspend fun analyze(url: String): BookRemoteModel? {
        return analyzers.firstOrNull { it.canAnalyze(url) }?.analyze(url)
    }
}