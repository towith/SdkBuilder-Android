package com.willbe.giftapp.appPipe.obj

import com.willbe.pipeline.obj.AppWidgetDTO
import com.willbe.pipeline.obj.App_DTO

class Context(var outputPath: String, var workingDir: String) {
    lateinit var app: App_DTO;
    lateinit var appWidgetDTOList: List<AppWidgetDTO>
}
