package io.kort.inbooks.data.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath
import org.koin.core.scope.Scope

fun Scope.createAppDataStore(name: String): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath {
        getDataStorePath("$name.preferences_pb").toPath()
    }
}

expect fun Scope.getDataStorePath(fileName: String): String