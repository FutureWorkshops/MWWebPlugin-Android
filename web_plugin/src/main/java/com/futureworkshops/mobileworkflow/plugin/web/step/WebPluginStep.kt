/*
 * Copyright (c) 2020 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.step

import android.os.Parcelable
import com.futureworkshops.mobileworkflow.model.configuration.NavigationItem
import com.futureworkshops.mobileworkflow.model.configuration.StepLink
import com.futureworkshops.mobileworkflow.model.step.PluginStep
import com.futureworkshops.mobileworkflow.plugin.web.data.WebViewAction
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WebPluginStep(
    @SerializedName("id") override val id: String,
    @SerializedName("type") override val type: String,
    @SerializedName("title") override val title: String,
    @SerializedName("links") override val links: List<StepLink>,
    @SerializedName("navigationItems") override val navigationItems: List<NavigationItem>,
    @SerializedName("optional") val optional: Boolean,
    @SerializedName("url") val url: String,
    @SerializedName("hideNavigation") val hideNavigation: Boolean?,
    @SerializedName("hideTopNavigationBar") val hideToolbar: Boolean?,
    @SerializedName("sharingEnabled") val sharingEnabled: Boolean?,
    @SerializedName("actions") val actions: List<WebViewAction>?
) : PluginStep(), Parcelable
