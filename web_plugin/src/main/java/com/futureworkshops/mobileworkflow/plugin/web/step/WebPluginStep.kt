/*
 * Copyright (c) 2020 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.step

import android.os.Parcelable
import com.futureworkshops.mobileworkflow.model.configuration.StepLink
import com.futureworkshops.mobileworkflow.model.step.PluginStep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WebPluginStep(
    override val id: String,
    override val type: String,
    override val title: String,
    override val links: List<StepLink>,
    val optional: Boolean,
    val url: String

) : PluginStep(), Parcelable
