package io.kort.inbooks.ui.pattern

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.kort.inbooks.ui.component.TextField

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    initialValue: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isSearching: Boolean = false,
) {
    TextField(
        modifier = modifier,
        title = null,
        initialValue = initialValue,
        onValueChange = onValueChange,
        placeholder = placeholder,
        end = {
            AnimatedVisibility(visible = isSearching) {
                CircularProgressIndicator(
                    modifier = Modifier.size(12.dp),
                    color = io.kort.inbooks.ui.token.system.System.colors.onBackgroundVariant,
                    strokeWidth = 1.dp
                )
            }
        },
        singleLine = true,
        rounded = true,
    )
}