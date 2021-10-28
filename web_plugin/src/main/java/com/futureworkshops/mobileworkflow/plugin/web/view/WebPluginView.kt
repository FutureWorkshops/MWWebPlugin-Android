/*
 * Copyright (c) 2020 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.view

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStep
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStepConfiguration
import com.futureworkshops.mobileworkflow.model.result.EmptyAnswerResult
import com.futureworkshops.mobileworkflow.model.result.FragmentStepResult


internal class WebPluginView(
    private val fragmentStepConfiguration: FragmentStepConfiguration,
    private val url: String
) : FragmentStep(fragmentStepConfiguration) {

    private lateinit var webView: WebView
    private lateinit var webPart: WebPart

    override fun createResults(): FragmentStepResult<EmptyAnswerResult> {
        return FragmentStepResult(
            identifier = id.id,
            answer = EmptyAnswerResult()
        )
    }

    override fun isValidInput(): Boolean = true

    override fun setupViews() {
        super.setupViews()
        context?.let {
            webPart = WebPart(it)
            webPart.style(surveyTheme)
            content.add(webPart)
            webView = fragmentStepConfiguration.mobileWorkflowServices.viewFactory.createWebView(it)
            webView.webViewClient = WebViewClient()
            @SuppressLint("SetJavaScriptEnabled")
            webView.settings.javaScriptEnabled = true
            webPart.view.webViewContainer.addView(webView)

            viewUrl()
        }
    }

    private fun viewUrl() {
        webView.loadUrl(url)
    }

    override fun back() = if (webView.canGoBack()) webView.goBack() else super.back()
}
