/*
 * Copyright (c) 2020 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.step

import android.os.Parcelable
import com.futureworkshops.mobileworkflow.model.step.PluginStep
import kotlinx.parcelize.Parcelize

@Parcelize
data class WebPluginStep(
    override val type: String,
    override val title: String,
    val optional: Boolean,
    val url: String

) : PluginStep(), Parcelable
