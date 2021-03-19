/*
 * Copyright (c) 2020 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.view

import android.webkit.WebView
import android.webkit.WebViewClient
import com.futureworkshops.mobileworkflow.data.webview.IViewFactory
import com.futureworkshops.mobileworkflow.StepIdentifier
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStep
import com.futureworkshops.mobileworkflow.result.QuestionResult
import com.futureworkshops.mobileworkflow.result.question_results.EmptyQuestionResult
import com.futureworkshops.mobileworkflow.ui.custom_steps.pdf.WebPart
import kotlinx.android.synthetic.main.web_step.*


internal class WebPluginView(
    id: StepIdentifier,
    isOptional: Boolean,
    title: String,
    nextButtonText: String,
    private val viewFactory: IViewFactory,
    private val url: String
) : FragmentStep(id, isOptional, title, null, nextButtonText, viewFactory) {

    private lateinit var webView: WebView
    private lateinit var webPart: WebPart

    override fun createResults(): QuestionResult = EmptyQuestionResult(id, startDate)

    override fun isValidInput(): Boolean = true

    override fun setupViews() {
        super.setupViews()
        context?.let {
            webPart = WebPart(it)
            webPart.style(surveyTheme)
            content.add(webPart)
            webView = viewFactory.createWebView(it)
            webView.webViewClient = WebViewClient()
            webView.settings.javaScriptEnabled = true
            webViewContainer.addView(webView)

            viewUrl()
        }
    }

    private fun viewUrl() {
        webView.loadUrl(url)
    }
}
