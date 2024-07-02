package ui.foundation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.kamel.image.config.resourcesFetcher
import io.kamel.image.config.resourcesIdMapper

@Composable
actual fun getKamelConfig(): KamelConfig {
    val context = LocalContext.current
    return remember(context) {
        KamelConfig {
            takeFrom(KamelConfig.Default)
            resourcesFetcher(context)
            resourcesIdMapper(context)
        }
    }
}