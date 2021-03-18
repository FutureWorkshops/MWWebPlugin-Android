/*
 * Copyright (c) 2020 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.view

import com.futureworkshops.mobileworkflow.model.WorkflowServiceResponse
import com.futureworkshops.mobileworkflow.surveykit.StepIdentifier
import com.futureworkshops.mobileworkflow.surveykit.backend.views.step.StepView
import com.futureworkshops.mobileworkflow.surveykit.result.StepResult
import com.futureworkshops.mobileworkflow.surveykit.services.MobileWorkflowServices
import com.futureworkshops.mobileworkflow.surveykit.steps.Step

internal class UIWebPluginStep(
    private val title: String,
    private val url: String,
    override var isOptional: Boolean = false,
    override val id: StepIdentifier = StepIdentifier(),
    override val uuid: String,
    private val nextButtonText: String = "Next"
) : Step {

    override fun createView(
        stepResult: StepResult?,
        mobileWorkflowServices: MobileWorkflowServices,
        workflowServiceResponse: WorkflowServiceResponse,
        selectedWorkflowId: String
    ): StepView = WebPluginView(
        id = id,
        isOptional = isOptional,
        title = mobileWorkflowServices.localizationService.getTranslation(title),
        nextButtonText = mobileWorkflowServices.localizationService.getTranslation(nextButtonText),
        url = url,
        viewFactory = mobileWorkflowServices.viewFactory
    )

}