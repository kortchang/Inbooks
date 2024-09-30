package io.kort.inbooks.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.kort.inbooks.ui.foundation.dontClearFocus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import io.kort.inbooks.ui.token.system.System

@Composable
fun TextField(
    initialValue: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    placeholder: String? = null,
    end: @Composable () -> Unit = {},
    singleLine: Boolean = false,
    rounded: Boolean = false,
) {
    var value by remember { mutableStateOf(TextFieldValue(initialValue)) }
    val updatedOnValueChange by rememberUpdatedState(onValueChange)
    LaunchedEffect(Unit) {
        snapshotFlow { value }
            .debounce(500)
            .distinctUntilChanged()
            .collectLatest { updatedOnValueChange(it.text) }
    }

    BasicTextField(
        modifier = modifier.dontClearFocus(),
        value = value,
        onValueChange = { value = it },
        textStyle = TextStyle(
            color = System.colors.onBackground,
            fontSize = 16.sp
        ),
        cursorBrush = SolidColor(System.colors.onBackgroundVariant),
        singleLine = singleLine,
        decorationBox = { innerTextField ->
            InteractionBox(rounded = rounded) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    title?.let {
                        Text(
                            text = it,
                            color = System.colors.onBackground,
                            fontSize = 16.sp,
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        var placeholderIsMultipleLine by remember { mutableStateOf(false) }
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = if (placeholderIsMultipleLine) Alignment.TopStart else Alignment.CenterStart
                        ) {
                            if (placeholder != null && value.text.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    color = System.colors.onBackgroundVariant,
                                    fontSize = 16.sp,
                                    onTextLayout = {
                                        placeholderIsMultipleLine = it.lineCount > 1
                                    }
                                )
                            }
                            innerTextField()
                        }
                        end()
                    }
                }
            }
        }
    )
}