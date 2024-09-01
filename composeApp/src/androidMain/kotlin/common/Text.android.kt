package common

actual fun String.format(vararg arguments: Any): String = format(args = arguments)