package io.kort.inbooks.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getIsLoggedIn(): Flow<Boolean>
}