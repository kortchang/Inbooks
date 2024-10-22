package io.kort.inbooks.common

enum class Platform {
    Android,
    iOS,
}

expect fun getPlatform(): Platform
