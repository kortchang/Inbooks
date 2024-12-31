package io.kort.inbooks.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import io.kort.inbooks.ui.foundation.dontClearFocus
import io.kort.inbooks.ui.resource.Icons
import io.kort.inbooks.ui.resource.WarningTriangleSmall
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import io.kort.inbooks.ui.token.system.System


@Composable
fun TextField(
    initialValue: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    placeholder: String? = null,
    singleLine: Boolean = false,
    rounded: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    end: @Composable () -> Unit = {},
    additional: @Composable () -> Unit = {},
) {
    var value by remember { mutableStateOf(TextFieldValue(initialValue)) }
    val updatedOnValueChange by rememberUpdatedState(onValueChange)
    LaunchedEffect(Unit) {
        snapshotFlow { value }
            .distinctUntilChanged()
            .collectLatest { updatedOnValueChange(it.text) }
    }

    TextField(
        value = value,
        onValueChange = { value = it },
        modifier = modifier,
        title = title,
        placeholder = placeholder,
        singleLine = singleLine,
        rounded = rounded,
        visualTransformation = visualTransformation,
        end = end,
        additional = additional,
    )
}

@Composable
fun TextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    placeholder: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = false,
    rounded: Boolean = false,
    end: @Composable () -> Unit = {},
    additional: @Composable () -> Unit = {},
) {
    BasicTextField(
        modifier = modifier.dontClearFocus(),
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            color = System.colors.onBackground,
            fontSize = 16.sp
        ),
        cursorBrush = SolidColor(System.colors.onBackgroundVariant),
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        decorationBox = { innerTextField ->
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                InteractionBox(rounded = rounded) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        val hasEntered = value.text.isNotEmpty()
                        title?.let {
                            Text(
                                text = it,
                                color = if (hasEntered) System.colors.onBackgroundVariant else System.colors.onBackground,
                                fontWeight = FontWeight.W500,
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
                                if (placeholder != null && hasEntered.not()) {
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
                additional()
            }
        }
    )
}

object TextFieldDefaults {
    @Composable
    fun WarningText(text: String) {
        WarningText(AnnotatedString(text))
    }


    @Composable
    fun WarningText(text: AnnotatedString) {
        val textWithIcon = remember(text) {
            buildAnnotatedString {
                appendInlineContent("warning_icon")
                append(text)
            }
        }

        val inlineContent = remember {
            mapOf(
                "warning_icon" to InlineTextContent(
                    placeholder = Placeholder(
                        width = 1.em,
                        height = 1.em,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.AboveBaseline
                    ),
                    children = {
                        Icon(
                            imageVector = Icons.WarningTriangleSmall,
                            contentDescription = null,
                            tint = System.colors.warning,
                        )
                    }
                )
            )
        }

        Text(
            text = textWithIcon,
            color = System.colors.warning,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            inlineContent = inlineContent,
        )
    }
}