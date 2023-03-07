/*
 * Copyright (c) 2020 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.view


import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStep
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStepConfiguration
import com.futureworkshops.mobileworkflow.model.AppServiceResponse
import com.futureworkshops.mobileworkflow.model.result.AnswerResult
import com.futureworkshops.mobileworkflow.services.ServiceBox
import com.futureworkshops.mobileworkflow.steps.DataTitle
import com.futureworkshops.mobileworkflow.steps.Step

internal data class UIWebPluginStep(
    override val title: String,
    private val url: String,
    override val id: String,
    private val hideNavigation: Boolean,
    private val hideToolbar: Boolean,
    private val nextButtonText: String = "Next",
    private val showShareOption: Boolean
) : Step, DataTitle {

    override fun copyWithNewTitle(title: String): Step {
        return UIWebPluginStep(
            title = title,
            url = url,
            id = id,
            hideNavigation = hideNavigation,
            hideToolbar = hideToolbar,
            nextButtonText = nextButtonText,
            showShareOption = showShareOption
        )
    }

    override fun createView(
        stepResult: AnswerResult?,
        services: ServiceBox,
        appServiceResponse: AppServiceResponse
    ): FragmentStep {
        val resolvedURL = services.urlTaskBuilder.urlHelper.resolveUrl(
            appServiceResponse.server,
            url,
            appServiceResponse.session
        ) ?: url

        return WebPluginView(
            FragmentStepConfiguration(
                title = if(hideToolbar) null else services.localizationService.getTranslation(title),
                text = null,
                nextButtonText = services.localizationService.getTranslation(nextButtonText),
                services = services
            ),
            url = resolvedURL,
            hideNavigation = hideNavigation,
            hideToolbar = hideToolbar,
            showShareOption = showShareOption
        )
    }
}