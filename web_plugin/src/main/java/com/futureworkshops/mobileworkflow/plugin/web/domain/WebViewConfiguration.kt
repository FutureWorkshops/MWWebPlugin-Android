/*
 * Copyright (c) 2023 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.domain

import com.futureworkshops.mobileworkflow.extensions.get
import com.futureworkshops.mobileworkflow.model.AppServiceResponse
import com.futureworkshops.mobileworkflow.plugin.web.data.RestConfiguration
import com.futureworkshops.mobileworkflow.services.ServiceBox

class WebViewConfiguration(
    url: String,
    hideNavigation: Boolean,
    hideToolbar: Boolean,
    showShareOption: Boolean,
    val remoteConfiguration: Boolean,
    private val services: ServiceBox,
    private val appServiceResponse: AppServiceResponse
) {
    var url: String?
        private set
    var hideNavigation: Boolean = hideNavigation
        private set
    var hideToolbar: Boolean = hideToolbar
        private set
    var showShareOption: Boolean = showShareOption
        private set

    init {
        if (remoteConfiguration) {
            this.url = null
        } else {
            this.url = services.urlTaskBuilder.urlHelper.resolveUrl(
                appServiceResponse.server,
                url,
                appServiceResponse.session
            ) ?: url
        }
    }

    suspend fun loadConfiguration(): Boolean {
        if (!remoteConfiguration) { return false }
        val baseURL = url ?: return false
        val response: RestConfiguration = services.get(baseURL)
        hideNavigation = response.hideNavigation
        hideToolbar = response.hideToolbar
        showShareOption = response.showShareOption
        return true
    }
}