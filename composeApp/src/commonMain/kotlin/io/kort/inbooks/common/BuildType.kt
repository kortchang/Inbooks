package io.kort.inbooks.common

enum class BuildType {
    Debug, Release
}

expect fun getBuildType(): BuildType