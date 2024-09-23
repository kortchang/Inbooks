package io.kort.inbooks.data.source

import android.content.Context
import org.koin.core.scope.Scope

actual fun Scope.getDataStorePath(fileName: String): String {
    return get<Context>().filesDir.resolve(fileName).absolutePath
}