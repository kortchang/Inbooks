package io.kort.inbooks.ui.resource

import io.kort.inbooks.common.Platform
import io.kort.inbooks.common.getPlatform

val Icons.Share
    get() =
        when (getPlatform()) {
            Platform.Android -> Icons.ShareAndroid
            Platform.iOS -> Icons.ShareiOS
        }
