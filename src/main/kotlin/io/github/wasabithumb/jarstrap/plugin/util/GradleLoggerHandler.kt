package io.github.wasabithumb.jarstrap.plugin.util

import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logger
import java.util.logging.Handler
import java.util.logging.Level
import java.util.logging.LogRecord

internal class GradleLoggerHandler(
    private val gradle: Logger
) : Handler() {

    override fun publish(record: LogRecord?) {
        if (record == null) return
        val level: LogLevel = when (record.level) {
            Level.SEVERE -> LogLevel.ERROR
            Level.WARNING -> LogLevel.WARN
            Level.INFO -> LogLevel.DEBUG // Intentional override for our use case
            Level.CONFIG -> LogLevel.LIFECYCLE
            Level.FINE, Level.FINER, Level.FINEST -> LogLevel.DEBUG
            else -> LogLevel.QUIET
        }
        val raised = record.thrown
        val message = record.message ?: ""
        if (raised != null) {
            this.gradle.log(level, message, raised)
        } else {
            this.gradle.log(level, message)
        }
    }

    override fun flush() { }

    override fun close() { }

    //

    companion object {

        fun wrap(gradleLogger: Logger): java.util.logging.Logger {
            val java = java.util.logging.Logger.getAnonymousLogger()
            for (handler in java.handlers) java.removeHandler(handler)

            val handler = GradleLoggerHandler(gradleLogger)
            java.addHandler(handler)

            return java
        }

    }

}