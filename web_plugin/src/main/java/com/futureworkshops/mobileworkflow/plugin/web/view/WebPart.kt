/*
 * Copyright (c) 2020 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.ui.custom_steps.pdf

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.futureworkshops.mobileworkflow.plugin.web.R
import com.futureworkshops.mobileworkflow.surveykit.SurveyTheme
import com.futureworkshops.mobileworkflow.surveykit.backend.helpers.extensions.toColorStateList
import com.futureworkshops.mobileworkflow.surveykit.backend.views.main_parts.StyleablePart
import kotlinx.android.synthetic.main.web_step.view.*

class WebPart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleRes), StyleablePart {

    val view: View = View.inflate(context, R.layout.web_step, this)

    override fun style(surveyTheme: SurveyTheme) {
        val colorStateList = surveyTheme.themeColor.toColorStateList()
        view.progressBar.indeterminateTintList = colorStateList
    }

}