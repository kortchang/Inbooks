package io.kort.inbooks.ui.foundation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

@Composable
operator fun PaddingValues.plus(values: PaddingValues): PaddingValues {
    val layoutDirection = LocalLayoutDirection.current
    return this.plus(values, layoutDirection)
}

fun PaddingValues.plus(values: PaddingValues, layoutDirection: LayoutDirection): PaddingValues {
    return PaddingValues(
        start = this.calculateStartPadding(layoutDirection) + values.calculateStartPadding(layoutDirection),
        top = this.calculateTopPadding() + values.calculateTopPadding(),
        end = this.calculateEndPadding(layoutDirection) + values.calculateEndPadding(layoutDirection),
        bottom = this.calculateBottomPadding() + values.calculateBottomPadding()
    )
}