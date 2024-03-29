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
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.lifecycleScope
import com.futureworkshops.mobileworkflow.backend.helpers.extensions.toColorStateList
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStep
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStepConfiguration
import com.futureworkshops.mobileworkflow.domain.service.log.Logger
import com.futureworkshops.mobileworkflow.extensions.buildChooserShareText
import com.futureworkshops.mobileworkflow.extensions.colorOnPrimarySurface
import com.futureworkshops.mobileworkflow.extensions.getDrawableIdentifier
import com.futureworkshops.mobileworkflow.model.result.AnswerResult
import com.futureworkshops.mobileworkflow.model.result.EmptyAnswerResult
import com.futureworkshops.mobileworkflow.plugin.web.R
import com.futureworkshops.mobileworkflow.plugin.web.domain.WebViewConfiguration
import com.futureworkshops.mobileworkflow.plugin.web.view.webview.LoggerWebChromeClient
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

internal class WebPluginView(
    private val fragmentStepConfiguration: FragmentStepConfiguration,
    private val config: WebViewConfiguration,
    private val logger: Logger = Logger.sharedInstance,
) : FragmentStep(fragmentStepConfiguration) {

    private lateinit var webView: WebView
    private lateinit var webPart: WebPart

    private val shouldShowNextBottomBar: Boolean
        get() = if (config.hideNavigation) { false } else { showContinue }
    private val shouldShowShareBottomBar: Boolean
        get() = if (config.hideNavigation) { false } else { config.showShareOption }

    override fun getStepOutput(): AnswerResult = EmptyAnswerResult()
    override fun isValidInput(): Boolean = true

    private var isShareShown: Boolean = false

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
        webView.visibility = View.INVISIBLE
        webPart.view.webViewContainer.addView(webView)

        enableFullScreen()
        setUpFooter()
        viewUrl()
    }

    override fun onResume() {
        super.onResume()
        isShareShown = false
    }

    override fun onViewCreated() {
        super.onViewCreated()
        header.visibility = if (config.hideNavigation) View.GONE else View.VISIBLE
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

    private fun setUpFooter() {
        webPart.view.bottomBarContainer.visibility = if (shouldShowNextBottomBar || shouldShowShareBottomBar) {
            View.VISIBLE
        } else {
            View.GONE
        }
        setUpShareButton(shouldShowShareBottomBar)
        setUpNextButton(shouldShowNextBottomBar)

        webPart.view.bottomAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.next_menu_item -> {
                    footer.onContinue()
                    true
                }
                R.id.share_menu_item -> {
                    shareUrl()
                    true
                }
                else -> false
            }
        }
    }

    private fun setUpNextButton(showButton: Boolean) {
        if (showButton) {
            val bottomBarMenu = webPart.view.bottomAppBar.menu
            val menuItem = bottomBarMenu.add(
                R.id.main_menu_group,
                R.id.next_menu_item,
                0,
                fragmentStepConfiguration.nextButtonText
            )
            menuItem?.apply {
                setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            }
        }
    }

    private fun setUpShareButton(showButton: Boolean) {
        if (showButton) {
            val bottomBarMenu = webPart.view.bottomAppBar.menu
            configureShareMenu(bottomBarMenu, true)
        }
    }


    private fun viewUrl() {
        CoroutineScope(Dispatchers.IO).launch {
            showLoading()

            if (config.loadConfiguration()) {
                reloadUIElements()
            }

            val url = config.url
            if (url.isNullOrEmpty()) {
                hideLoading()
                showUnableToLoad()
            } else {
                loadUrl(url)
            }
        }
    }

    private fun reloadUIElements() = CoroutineScope(Dispatchers.Main).launch {
        header.visibility = if (config.hideNavigation) View.GONE else View.VISIBLE
        activity?.let { ActivityCompat.invalidateOptionsMenu(it) }
        setUpFooter()
    }

    private fun loadUrl(url: String) = CoroutineScope(Dispatchers.Main).launch {
        webView.loadUrl(url)
    }

    private fun showUnableToLoad() = CoroutineScope(Dispatchers.Main).launch {
        val safeContext = context ?: return@launch
        Toast.makeText(
            safeContext,
            getString(R.string.unable_to_load),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun back() {
        if (webView.canGoBack()) webView.goBack() else super.back()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        configureShareMenu(menu, config.showShareOption && config.hideNavigation)

        if (config.hideNavigation && showContinue) {
            val menuItem = menu.add(
                R.id.main_menu_group,
                R.id.next_menu_item,
                0,
                fragmentStepConfiguration.nextButtonText
            )
            menuItem?.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }

        config.actions.forEachIndexed { index, action ->
            val menuItem = menu.add(
                R.id.action_menu_item,
                index,
                index,
                ""
            )
            action.materialIconName
                .let(requireContext()::getDrawableIdentifier)
                .let { AppCompatResources.getDrawable(requireContext(), it) }
                ?.apply {
                    DrawableCompat.setTintList(this, requireContext().colorOnPrimarySurface.toColorStateList())
                    menuItem?.icon = this
                }
            menuItem?.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when {
            item.groupId == R.id.action_menu_item -> {
                performActionAt(item.itemId)
                true
            }
            item.itemId == android.R.id.home -> {
                //For the toolbar home, we are always going one step up.
                super.back()
                true
            }
            item.itemId == R.id.share_menu_item -> {
                shareUrl()
            }
            !config.hideNavigation || item.itemId != R.id.next_menu_item -> {
                super.onOptionsItemSelected(item)
            }
            else -> {
                footer.onContinue()
                true
            }
        }
    }

    private fun performActionAt(actionIndex: Int) = CoroutineScope(Dispatchers.IO).launch {
        val action = config.actions.getOrNull(actionIndex) ?: return@launch
        showLoading()
        try {
            val shouldReloadPage = config.performAction(action)
            reloadUIElements()
            if (shouldReloadPage) {
                val url = config.url
                if (url.isNullOrEmpty()) {
                    hideLoading()
                    showUnableToLoad()
                } else {
                    loadUrl(url)
                }
            } else {
                hideLoading()
            }
        } catch (_: Exception) {
            hideLoading()
            showUnableToLoad()
        }
    }

    private fun shareUrl(): Boolean {
        val url = config.url ?: return false

        if (!isShareShown) {
            context?.startActivity(
                buildChooserShareText(url)
            )
            isShareShown = true
        }

        return true
    }

    private fun configureShareMenu(menu: Menu, isVisble: Boolean) {
        val shareMenu = menu.add(Menu.NONE, R.id.share_menu_item, 0, R.string.menu_item_share)
        shareMenu.icon = ContextCompat.getDrawable(requireContext(), com.futureworkshops.mobileworkflow.R.drawable.ic_share)?.apply {
            DrawableCompat.setTintList(this, requireContext().colorOnPrimarySurface.toColorStateList())
        }
        shareMenu.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)

        shareMenu.isVisible = isVisble

    }

    private fun showLoading() = CoroutineScope(Dispatchers.Main).launch {
        if (webPart.progressBar.visibility == View.GONE) {
            webPart.progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideLoading() = CoroutineScope(Dispatchers.Main).launch {
        if (webPart.progressBar.visibility == View.VISIBLE) {
            webView.visibility = View.VISIBLE
            webPart.progressBar.visibility = View.GONE
        }
    }
}
