/*
 * Copyright (c) 2023 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web

import com.futureworkshops.mobileworkflow.domain.DeserializeStep
import com.futureworkshops.mobileworkflow.plugin.web.step.RestWebViewStep
import com.futureworkshops.mobileworkflow.plugin.web.view.UIWebPluginStep
import com.futureworkshops.mobileworkflow.steps.Step

class RestWebPlugin : DeserializeStep<RestWebViewStep>(
    type = "io.app-rail.webview.rest-web-view",
    classT = RestWebViewStep::class.java
) {

    override fun createUIStep(step: RestWebViewStep): Step =
        UIWebPluginStep(
            title = step.title,
            id = step.id,
            url = step.url,
            hideNavigation = false,
            hideToolbar = false,
            showShareOption = false,
            remoteConfiguration = true
        )
}