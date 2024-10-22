package io.kort.inbooks.domain.repository

import io.kort.inbooks.domain.model.settings.BookListDisplayStyle
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun isOnboarded(): Boolean
    suspend fun markIsOnboarded()
    fun getBookListDisplayStyleFlow(): Flow<BookListDisplayStyle?>
    suspend fun updateBookListDisplayStyle(value: BookListDisplayStyle)
}