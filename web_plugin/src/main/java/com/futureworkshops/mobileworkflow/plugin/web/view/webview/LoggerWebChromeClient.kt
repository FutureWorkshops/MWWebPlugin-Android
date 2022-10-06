/*
 * Copyright (c) 2022 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.view.webview

import android.content.Context
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import com.futureworkshops.mobileworkflow.domain.service.log.Logger
import com.futureworkshops.mobileworkflow.model.log.LogLevel

val ConsoleMessage.MessageLevel.loggerLevel: LogLevel
    get() = when(this) {
        ConsoleMessage.MessageLevel.DEBUG -> LogLevel.DEBUG
        ConsoleMessage.MessageLevel.ERROR -> LogLevel.ERROR
        ConsoleMessage.MessageLevel.LOG -> LogLevel.INFORMATION
        ConsoleMessage.MessageLevel.TIP -> LogLevel.INFORMATION
        ConsoleMessage.MessageLevel.WARNING -> LogLevel.INFORMATION
    }

fun ConsoleMessage.logMessage(
    context: Context,
    logger: Logger,
    stackTraceElement: StackTraceElement = Thread.currentThread().stackTrace[3]
) = logger.log(
    context = context,
    message = "${sourceId()}[${lineNumber()}]: ${message()}",
    logLevel = messageLevel().loggerLevel,
    stackTraceElement = stackTraceElement
)

open class LoggerWebChromeClient(
    private val context: Context,
    private val logger: Logger
): WebChromeClient() {
    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        consoleMessage?.logMessage(context, logger)
        return true
    }
}