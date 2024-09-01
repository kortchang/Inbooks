package ui.token

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import book.composeapp.generated.resources.Res
import book.composeapp.generated.resources.rubik_variable
import org.jetbrains.compose.resources.Font

@Composable
fun RubikFontFamily() = FontFamily(
    listOf(
        Font(
            Res.font.rubik_variable,
            weight = FontWeight.W300,
        ),
        Font(
            Res.font.rubik_variable,
            weight = FontWeight.W400,
        ),
        Font(
            Res.font.rubik_variable,
            weight = FontWeight.W500,
        ),
        Font(
            Res.font.rubik_variable,
            weight = FontWeight.W600,
        )
    )
)