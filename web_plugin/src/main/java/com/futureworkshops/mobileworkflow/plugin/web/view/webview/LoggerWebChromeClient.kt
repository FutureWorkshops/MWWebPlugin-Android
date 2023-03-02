/*
 * Copyright (c) 2022 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.view.webview

import android.app.Activity
import android.content.Context
import android.view.View
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.widget.FrameLayout
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
    private val logger: Logger,
    private val activity: Activity?
): WebChromeClient() {
    private var customView: View? = null

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        consoleMessage?.logMessage(context, logger)
        return true
    }

    override fun onHideCustomView() {
        activity?.let {
            (it.window.decorView as FrameLayout).removeView(customView)
            customView = null
        }
    }

    override fun onShowCustomView(paramView: View, paramCustomViewCallback: CustomViewCallback) {
        if (customView != null) {
            onHideCustomView()
            return
        }
        activity?.let {
            customView = paramView
            (it.window.decorView as FrameLayout).addView(customView,  FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
        }
    }
}