package ui.feature.bookdetail.pattern

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import book.composeapp.generated.resources.Res
import book.composeapp.generated.resources.collected_book_reading_reason_placeholder
import book.composeapp.generated.resources.collected_book_reading_reason_title
import book.composeapp.generated.resources.icon_binocular
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import ui.feature.bookdetail.screen.collected.CollectedBookDetailContentScope

fun CollectedBookDetailContentScope.reason(
    value: String,
    onValueChange: (String) -> Unit
) {
    item {
        DetailSection(
            title = {
                DetailSectionTitle(
                    modifier = Modifier.fillMaxWidth(),
                    icon = painterResource(Res.drawable.icon_binocular),
                    title = stringResource(Res.string.collected_book_reading_reason_title)
                )

            }
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                initialValue = value,
                onValueChange = onValueChange,
            )
        }
    }
}

@Composable
private fun TextField(
    modifier: Modifier = Modifier,
    initialValue: String,
    onValueChange: (String) -> Unit
) {
    var value by remember { mutableStateOf(TextFieldValue(initialValue)) }
    LaunchedEffect(value) {
        onValueChange(value.text)
    }
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = { value = it },
        interactionSource = interactionSource,
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = Color(0xFF525252),
        ),
        decorationBox = { innerTextField ->
            Box(contentAlignment = Alignment.CenterStart) {
                if (value.text.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.collected_book_reading_reason_placeholder),
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        color = Color(0xFF7C7C7C),
                    )
                }
                innerTextField()
            }
        }
    )
}