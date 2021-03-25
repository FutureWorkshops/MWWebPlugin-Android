/*
 * Copyright (c) 2020 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.view

import android.webkit.WebView
import android.webkit.WebViewClient
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStep
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStepConfiguration
import com.futureworkshops.mobileworkflow.result.FragmentStepResult
import com.futureworkshops.mobileworkflow.result.step_results.EmptyStepResult
import com.futureworkshops.mobileworkflow.ui.custom_steps.pdf.WebPart
import kotlinx.android.synthetic.main.web_step.*


internal class WebPluginView(
    private val fragmentStepConfiguration: FragmentStepConfiguration,
    private val url: String
) : FragmentStep(fragmentStepConfiguration) {

    private lateinit var webView: WebView
    private lateinit var webPart: WebPart

    override fun createResults(): FragmentStepResult = EmptyStepResult(id, startDate)

    override fun isValidInput(): Boolean = true

    override fun setupViews() {
        super.setupViews()
        context?.let {
            webPart = WebPart(it)
            webPart.style(surveyTheme)
            content.add(webPart)
            webView = fragmentStepConfiguration.mobileWorkflowServices.viewFactory.createWebView(it)
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
