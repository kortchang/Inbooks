package ui.foundation

import androidx.compose.runtime.Composable
import io.kamel.core.config.KamelConfig
import io.kamel.image.config.Default

@Composable
actual fun getKamelConfig(): KamelConfig {
    return KamelConfig.Default
}