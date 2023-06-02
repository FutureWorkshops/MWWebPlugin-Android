/*
 * Copyright (c) 2023 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.domain

import com.futureworkshops.mobileworkflow.model.AppServiceResponse
import com.futureworkshops.mobileworkflow.services.ServiceBox

class WebViewConfiguration(
    url: String,
    hideNavigation: Boolean,
    hideToolbar: Boolean,
    showShareOption: Boolean,
    private val services: ServiceBox,
    private val appServiceResponse: AppServiceResponse
) {
    var url: String = services.urlTaskBuilder.urlHelper.resolveUrl(
        appServiceResponse.server,
        url,
        appServiceResponse.session
    ) ?: url

    var hideNavigation: Boolean = hideNavigation
        private set
    var hideToolbar: Boolean = hideToolbar
        private set
    var showShareOption: Boolean = showShareOption
        private set
}