/*
 * Copyright (c) 2023 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.domain

import com.futureworkshops.mobileworkflow.data.network.task.URLIAsyncTask
import com.futureworkshops.mobileworkflow.extensions.URLNotFoundException
import com.futureworkshops.mobileworkflow.extensions.get
import com.futureworkshops.mobileworkflow.model.AppServiceResponse
import com.futureworkshops.mobileworkflow.plugin.web.data.RestConfiguration
import com.futureworkshops.mobileworkflow.plugin.web.data.WebViewAction
import com.futureworkshops.mobileworkflow.services.ServiceBox

class WebViewConfiguration(
    url: String,
    hideNavigation: Boolean,
    hideToolbar: Boolean,
    showShareOption: Boolean,
    actions: List<WebViewAction>?,
    private val remoteConfiguration: Boolean,
    private val services: ServiceBox,
    appServiceResponse: AppServiceResponse
) {
    private val baseURL: String = services.urlTaskBuilder.urlHelper.resolveUrl(
        appServiceResponse.server,
        url,
        appServiceResponse.session
    ) ?: url

    var url: String?
        private set
    var hideNavigation: Boolean = hideNavigation
        private set
    var hideToolbar: Boolean = hideToolbar
        private set
    var showShareOption: Boolean = showShareOption
        private set
    var actions: List<WebViewAction> = actions ?: emptyList()
        private set

    init {
        if (remoteConfiguration) {
            this.url = null
        } else {
            this.url = baseURL
        }
    }

    suspend fun loadConfiguration(): Boolean {
        if (!remoteConfiguration) { return false }
        val response: RestConfiguration = services.get(baseURL)
        hideNavigation = response.hideNavigation ?: false
        hideToolbar = response.hideToolbar ?: false
        showShareOption = response.showShareOption ?: false
        url = response.url
        actions = response.actions ?: emptyList()
        return true
    }

    suspend fun performAction(action: WebViewAction): Boolean {
        val previousURL = url
        val resolvedUrl = services.urlTaskBuilder.resolve(action.url)
            ?: throw URLNotFoundException(action.url)
        services.serviceContainer.performNoResponse(
            URLIAsyncTask<Nothing, Nothing>(
                url = resolvedUrl,
                method = action.method,
                body = null,
                headers = emptyMap(),
                responseType = null,
                handleAuthentication = true
            ))
        return if (loadConfiguration()) {
            url != previousURL
        } else {
            false
        }
    }
}
