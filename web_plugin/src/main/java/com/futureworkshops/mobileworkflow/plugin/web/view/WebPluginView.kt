/*
 * Copyright (c) 2020 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.view

import android.annotation.SuppressLint
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStep
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStepConfiguration
import com.futureworkshops.mobileworkflow.domain.service.log.Logger
import com.futureworkshops.mobileworkflow.model.result.AnswerResult
import com.futureworkshops.mobileworkflow.model.result.EmptyAnswerResult
import com.futureworkshops.mobileworkflow.plugin.web.R
import com.futureworkshops.mobileworkflow.plugin.web.view.webview.LoggerWebChromeClient

internal class WebPluginView(
    private val fragmentStepConfiguration: FragmentStepConfiguration,
    private val url: String,
    private val hideNavigation: Boolean,
    private val logger: Logger = Logger.sharedInstance
) : FragmentStep(fragmentStepConfiguration) {

    private lateinit var webView: WebView
    private lateinit var webPart: WebPart

    override fun getStepOutput(): AnswerResult = EmptyAnswerResult()

    override fun isValidInput(): Boolean = true

    override fun setupViews() {
        super.setupViews()
        val safeContext = context ?: return

        webPart = WebPart(safeContext)
        content.add(webPart)

        webView = fragmentStepConfiguration.services.viewFactory.createWebView(safeContext)
        webView.webViewClient = object: WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                hideLoading()
                super.onPageFinished(view, url)
            }
        }
        webView.webChromeClient = LoggerWebChromeClient(safeContext, logger)
        @SuppressLint("SetJavaScriptEnabled")
        webView.settings.javaScriptEnabled = true
        webPart.view.webViewContainer.addView(webView)

        enableFullScreen()
        setUpFooter()
        viewUrl()
    }

    private fun enableFullScreen() {
        content.makeContainerMatchParent()
        content.hideFooterContainer()
    }

    private fun setUpFooter() = webPart.setUpButton(!hideNavigation && showContinue) { footer.onContinue() }

    private fun viewUrl() {
        showLoading()
        webView.loadUrl(url)
    }

    override fun back() = if (webView.canGoBack()) webView.goBack() else super.back()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (hideNavigation && showContinue) {
            val menuItem = menu.add(
                R.id.main_menu_group,
                R.id.next_menu_item,
                0,
                fragmentStepConfiguration.nextButtonText
            ) ?: return
            menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (!hideNavigation || item.itemId != R.id.next_menu_item) {
            return super.onOptionsItemSelected(item)
        }

        footer.onContinue()
        return true
    }

    private fun showLoading() {
        if (webPart.progressBar.visibility != View.GONE) { return }
        webPart.progressBar.visibility = View.VISIBLE
        webView.visibility = View.INVISIBLE
    }

    private fun hideLoading() {
        if (webPart.progressBar.visibility != View.VISIBLE) { return }
        webView.visibility = View.VISIBLE
        webPart.progressBar.visibility = View.GONE
    }
}
