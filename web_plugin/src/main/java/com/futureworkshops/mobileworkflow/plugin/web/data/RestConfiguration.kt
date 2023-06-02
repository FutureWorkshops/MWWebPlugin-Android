/*
 * Copyright (c) 2023 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RestConfiguration(
    @SerializedName("url") val url: String,
    @SerializedName("hideTopNavigationBar") val hideNavigation: Boolean,
    @SerializedName("hideNavigation") val hideToolbar: Boolean,
    @SerializedName("sharingEnabled") val showShareOption: Boolean,
    @SerializedName("actions") val actions: List<WebViewAction>?
): Parcelable