package io.kort.inbooks.common

import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

actual fun String.format(vararg arguments: Any): String = NSString.stringWithFormat(this, arguments.toList())