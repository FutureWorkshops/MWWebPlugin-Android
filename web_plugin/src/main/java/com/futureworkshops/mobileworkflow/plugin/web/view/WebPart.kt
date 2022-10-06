/*
 * Copyright (c) 2020 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ContentLoadingProgressBar
import androidx.viewbinding.ViewBinding
import com.futureworkshops.mobileworkflow.databinding.NextButtonBinding
import com.futureworkshops.mobileworkflow.plugin.web.R
import com.futureworkshops.mobileworkflow.plugin.web.databinding.WebStepBinding

class WebPart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleRes: Int = 0,
) : ConstraintLayout(context, attrs, defStyleRes) {

    class WebPartBinding(
        context: Context,
        @LayoutRes layout: Int,
        root: ViewGroup
    ): ViewBinding {
        private val innerView = WebStepBinding.bind(View.inflate(context, layout, root))
        val webViewContainer
            get() = innerView.webViewContainer
        val webViewNextButton = NextButtonBinding.bind(getRoot().findViewById(R.id.webViewNextButton))
        val progressBar
            get() = innerView.progressBar
        override fun getRoot(): View = innerView.root
    }

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    val view = WebPartBinding(context, R.layout.web_step, this)
    val progressBar: ContentLoadingProgressBar
        get() = view.progressBar

    fun setUpButton(showButton: Boolean, onClick: () -> Unit) {
        view.webViewNextButton.buttonContinue.apply {
            visibility = if (showButton) View.VISIBLE else View.GONE
            setOnClickListener { onClick() }
        }
    }
}