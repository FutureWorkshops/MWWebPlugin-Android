/*
 * Copyright (c) 2020 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.view

import android.webkit.WebView
import android.webkit.WebViewClient
import com.futureworkshops.mobileworkflow.data.webview.IWebViewFactory
import com.futureworkshops.mobileworkflow.surveykit.StepIdentifier
import com.futureworkshops.mobileworkflow.surveykit.backend.views.step.QuestionView
import com.futureworkshops.mobileworkflow.surveykit.result.QuestionResult
import com.futureworkshops.mobileworkflow.surveykit.result.question_results.EmptyQuestionResult
import com.futureworkshops.mobileworkflow.ui.custom_steps.pdf.WebPart
import kotlinx.android.synthetic.main.web_step.*


internal class WebPluginView(
    id: StepIdentifier,
    isOptional: Boolean,
    title: String,
    nextButtonText: String,
    private val webViewFactory: IWebViewFactory,
    private val url: String
) : QuestionView(id, isOptional, title, null, nextButtonText) {

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
            webView = webViewFactory.create(it)
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
