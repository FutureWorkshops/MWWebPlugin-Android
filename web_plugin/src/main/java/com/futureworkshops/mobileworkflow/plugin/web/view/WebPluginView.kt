/*
 * Copyright (c) 2020 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.view

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStep
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStepConfiguration
import com.futureworkshops.mobileworkflow.model.result.AnswerResult
import com.futureworkshops.mobileworkflow.model.result.EmptyAnswerResult


internal class WebPluginView(
    private val fragmentStepConfiguration: FragmentStepConfiguration,
    private val url: String,
    private val hideNavigation: Boolean
) : FragmentStep(fragmentStepConfiguration) {

    private lateinit var webView: WebView
    private lateinit var webPart: WebPart

    override fun getStepOutput(): AnswerResult = EmptyAnswerResult()

    override fun isValidInput(): Boolean = true

    override fun setupViews() {
        super.setupViews()
        context?.let {
            webPart = WebPart(it)
            content.add(webPart)
            webView = fragmentStepConfiguration.services.viewFactory.createWebView(it)
            webView.webViewClient = WebViewClient()
            @SuppressLint("SetJavaScriptEnabled")
            webView.settings.javaScriptEnabled = true
            webPart.view.webViewContainer.addView(webView)
            enableFullScreen()
            setUpFooter()
            viewUrl()
        }
    }

    private fun enableFullScreen() {
        content.makeContainerMatchParent()
        content.hideFooterContainer()
    }

    private fun setUpFooter() = webPart.setUpButton(footer.showContinue) { footer.onContinue() }

    private fun viewUrl() = webView.loadUrl(url)

    override fun back() = if (webView.canGoBack()) webView.goBack() else super.back()
}
