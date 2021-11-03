/*
 * Copyright (c) 2020 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.futureworkshops.mobileworkflow.plugin.web.R
import com.futureworkshops.mobileworkflow.SurveyTheme
import com.futureworkshops.mobileworkflow.backend.helpers.extensions.getTextColor
import com.futureworkshops.mobileworkflow.backend.helpers.extensions.toColorStateList
import com.futureworkshops.mobileworkflow.backend.views.main_parts.StyleablePart
import com.futureworkshops.mobileworkflow.plugin.web.databinding.WebStepBinding

class WebPart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0,
) : ConstraintLayout(context, attrs, defStyleRes), StyleablePart {

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    val view = WebStepBinding.bind(View.inflate(context, R.layout.web_step, this))

    override fun style(surveyTheme: SurveyTheme) {
        val colorStateList = surveyTheme.themeColor.toColorStateList()
        view.progressBar.indeterminateTintList = colorStateList
        view.webViewNextButton.buttonContinue.apply {
            backgroundTintList = colorStateList
            setTextColor(surveyTheme.themeColor.getTextColor())
        }
    }
}