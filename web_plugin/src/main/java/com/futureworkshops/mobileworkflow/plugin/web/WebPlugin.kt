/*
 * Copyright (c) 2020 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web

import com.futureworkshops.mobileworkflow.domain.DeserializeStep
import com.futureworkshops.mobileworkflow.plugin.web.step.WebPluginStep
import com.futureworkshops.mobileworkflow.plugin.web.view.UIWebPluginStep
import com.futureworkshops.mobileworkflow.StepIdentifier
import com.futureworkshops.mobileworkflow.steps.Step

internal class WebPlugin : DeserializeStep<WebPluginStep>(
    type = "io.mobileworkflow.WebView",
    classT = WebPluginStep::class.java
) {

    override fun createUIStep(step: WebPluginStep): Step =
        UIWebPluginStep(
            title = step.title,
            isOptional = step.optional,
            id = StepIdentifier(step.identifier),
            uuid = step.uuid,
            url = step.url
        )

}