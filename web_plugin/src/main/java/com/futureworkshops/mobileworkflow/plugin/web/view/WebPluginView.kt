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
import androidx.activity.addCallback
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStep
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStepConfiguration
import com.futureworkshops.mobileworkflow.domain.service.log.Logger
import com.futureworkshops.mobileworkflow.model.result.AnswerResult
import com.futureworkshops.mobileworkflow.model.result.EmptyAnswerResult
import com.futureworkshops.mobileworkflow.plugin.web.R
import com.futureworkshops.mobileworkflow.plugin.web.view.webview.LoggerWebChromeClient
import com.google.android.material.appbar.MaterialToolbar

internal class WebPluginView(
    private val fragmentStepConfiguration: FragmentStepConfiguration,
    private val url: String,
    private val hideNavigation: Boolean,
    private val hideToolbar: Boolean,
    private val logger: Logger = Logger.sharedInstance
) : FragmentStep(fragmentStepConfiguration) {

    private lateinit var webView: WebView
    private lateinit var webPart: WebPart

    override var showHeader: Boolean
        get() = !hideToolbar && super.showHeader
        set(value) { super.showHeader = value }
    private val shouldShowNextButton: Boolean
        get() = if (hideNavigation) { false } else { showContinue }

    override fun getStepOutput(): AnswerResult = EmptyAnswerResult()
    override fun isValidInput(): Boolean = true

    override fun setupViews() {
        super.setupViews()
        val safeContext = context ?: return

        webPart = WebPart(safeContext)
        content.clear()
        content.add(webPart)

        webView = fragmentStepConfiguration.services.viewFactory.createWebView(safeContext)
        webView.webViewClient = object: WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                hideLoading()
                super.onPageFinished(view, url)
            }
        }
        webView.webChromeClient = LoggerWebChromeClient(safeContext, logger, activity)
        webView.settings.apply {
            @SuppressLint("SetJavaScriptEnabled")
            javaScriptEnabled = true
            domStorageEnabled = true
        }
        webPart.view.webViewContainer.addView(webView)

        enableFullScreen()
        setUpFooter()
        viewUrl()
    }

    override fun onViewCreated() {
        super.onViewCreated()
        header.visibility = if (showHeader) View.VISIBLE else View.GONE
        toolbar.title = fragmentStepConfiguration.title
        (toolbar as? MaterialToolbar)?.isTitleCentered = true

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            back()
        }
    }

    private fun enableFullScreen() {
        content.makeContainerMatchParent()
        content.hideFooterContainer()
    }

    private fun setUpFooter() = webPart.setUpButton(shouldShowNextButton) {
        footer.onContinue()
    }

    private fun viewUrl() {
        showLoading()
        webView.loadUrl(url)
    }

    override fun back() {
        if (webView.canGoBack()) webView.goBack() else super.back()
    }

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
        if (item.itemId == android.R.id.home) {
            //For the toolbar home, we are always going one step up.
            super.back()
            return true
        }

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
