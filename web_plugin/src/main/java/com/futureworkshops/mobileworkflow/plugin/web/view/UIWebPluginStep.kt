/*
 * Copyright (c) 2020 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.view


import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStep
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStepConfiguration
import com.futureworkshops.mobileworkflow.model.AppServiceResponse
import com.futureworkshops.mobileworkflow.model.result.AnswerResult
import com.futureworkshops.mobileworkflow.services.ServiceBox
import com.futureworkshops.mobileworkflow.steps.Step

internal data class UIWebPluginStep(
    val title: String,
    private val url: String,
    override val id: String,
    private val nextButtonText: String = "Next"
) : Step {

    override fun createView(
        stepResult: AnswerResult?,
        services: ServiceBox,
        appServiceResponse: AppServiceResponse
    ): FragmentStep = WebPluginView(
        FragmentStepConfiguration(
                title = services.localizationService.getTranslation(title),
        text = null,
        nextButtonText = services.localizationService.getTranslation(nextButtonText),
        services = services),
        url = url
    )
}