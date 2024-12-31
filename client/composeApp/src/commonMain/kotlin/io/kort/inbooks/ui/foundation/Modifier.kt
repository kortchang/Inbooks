package io.kort.inbooks.ui.foundation

import androidx.compose.ui.Modifier

inline fun Modifier.thenIf(condition: Boolean, modifier: Modifier.() -> Modifier) =
    then(if (condition) modifier() else Modifier)