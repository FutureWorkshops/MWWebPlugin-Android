/*
 * Copyright (c) 2023 FutureWorkshops. All rights reserved.
 */

package com.futureworkshops.mobileworkflow.plugin.web.data

import android.os.Parcelable
import com.futureworkshops.mobileworkflow.data.network.task.URLMethod
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WebViewAction(
    @SerializedName("url") val url: String,
    @SerializedName("method") val method: URLMethod,
    @SerializedName("materialIconName") val materialIconName: String
): Parcelable
