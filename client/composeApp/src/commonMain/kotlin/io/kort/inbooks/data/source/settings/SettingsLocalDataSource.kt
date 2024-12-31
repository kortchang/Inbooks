package io.kort.inbooks.data.source.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.kort.inbooks.domain.model.settings.BookListDisplayStyle
import io.kort.inbooks.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapLatest

class SettingsLocalDataSource(private val dataStore: DataStore<Preferences>) : SettingsRepository {
    companion object {
        val IsOnboardedKey = booleanPreferencesKey("is_onboarded")
        val BookListDisplayStyleKey = stringPreferencesKey("book_list_display_style")
        val IsOnboardedSignUpKey = booleanPreferencesKey("is_onboarded_sign_up")
    }

    override suspend fun isOnboarded(): Boolean {
        return dataStore.data.firstOrNull()?.get(IsOnboardedKey) ?: false
    }

    override suspend fun markIsOnboarded() {
        dataStore.edit { settings ->
            settings[IsOnboardedKey] = true
        }
    }

    override fun getBookListDisplayStyleFlow(): Flow<BookListDisplayStyle?> {
        return dataStore.data.mapLatest { data ->
            data[BookListDisplayStyleKey]?.let { rawValue ->
                BookListDisplayStyle.entries.first { it.serializedName == rawValue }
            }
        }
    }

    override suspend fun updateBookListDisplayStyle(value: BookListDisplayStyle) {
        dataStore.edit { settings ->
            settings[BookListDisplayStyleKey] = value.serializedName
        }
    }

    override suspend fun isOnboardedSignUp(): Boolean {
        return dataStore.data.firstOrNull()?.get(IsOnboardedSignUpKey) ?: false
    }

    override suspend fun markIsOnboardedSignUp() {
        dataStore.edit { settings ->
            settings[IsOnboardedSignUpKey] = true
        }
    }
}