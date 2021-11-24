/*
 * Copyright (c) 2020 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.view

import com.futureworkshops.mobileworkflow.StepIdentifier
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStep
import com.futureworkshops.mobileworkflow.backend.views.step.FragmentStepConfiguration
import com.futureworkshops.mobileworkflow.model.WorkflowServiceResponse
import com.futureworkshops.mobileworkflow.model.result.AnswerResult
import com.futureworkshops.mobileworkflow.services.MobileWorkflowServices
import com.futureworkshops.mobileworkflow.steps.Step

internal data class UIWebPluginStep(
    val title: String,
    private val url: String,
    override var isOptional: Boolean = false,
    override val id: StepIdentifier = StepIdentifier(),
    override val uuid: String,
    private val nextButtonText: String = "Next"
) : Step {

    override fun createView(
        stepResult: AnswerResult?,
        mobileWorkflowServices: MobileWorkflowServices,
        workflowServiceResponse: WorkflowServiceResponse,
        selectedWorkflowId: String
    ): FragmentStep = WebPluginView(
        FragmentStepConfiguration(
        isOptional = isOptional,
        title = mobileWorkflowServices.localizationService.getTranslation(title),
        text = null,
        nextButtonText = mobileWorkflowServices.localizationService.getTranslation(nextButtonText),
        mobileWorkflowServices = mobileWorkflowServices),
        url = url
    )
}