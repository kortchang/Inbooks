package io.kort.inbooks.common

import io.kort.inbooks.BuildConfig

actual fun getBuildType(): BuildType = if(BuildConfig.DEBUG) BuildType.Debug else BuildType.Release