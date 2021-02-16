package com.futureworkshops.mobileworkflow.plugin.web

import com.futureworkshops.mobileworkflow.domain.PluginFactory

class WebPluginFactory : PluginFactory(
    listOf(WebPlugin())
)