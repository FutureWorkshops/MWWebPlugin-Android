package com.futureworkshops.mobileworkflow.plugin.web

import com.futureworkshops.mobileworkflow.domain.PluginFactory
import com.futureworkshops.mobileworkflow.model.plugin.PluginInformation

class WebPluginFactory : PluginFactory(
    listOf(WebPlugin())
) {
    override fun getInformation(): PluginInformation = PluginInformation(
        name = BuildConfig.LIBRARY_PACKAGE_NAME,
        version = BuildConfig.PLUGIN_VERSION
    )
}