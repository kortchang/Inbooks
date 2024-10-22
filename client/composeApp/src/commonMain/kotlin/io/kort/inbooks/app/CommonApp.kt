package io.kort.inbooks.app

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import io.kort.inbooks.common.BuildType
import io.kort.inbooks.common.getBuildType

class CommonApp {
    fun onCreate() {
        setupLogger()
    }

    private fun setupLogger() {
        if (getBuildType() == BuildType.Release) {
            Logger.setLogWriters(
                object : LogWriter() {
                    override fun isLoggable(tag: String, severity: Severity): Boolean = false
                    override fun log(severity: Severity, message: String, tag: String, throwable: Throwable?) {
                        // Do nothing
                    }
                }
            )
        }
    }
}