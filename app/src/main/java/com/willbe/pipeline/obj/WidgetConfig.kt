package com.willbe.giftapp.appPipe.obj

import com.willbe.pipeline.obj.AppWidgetDTO
import com.willbe.pipeline.obj.App_DTO

abstract class WidgetConfig(appWidgetDTO: AppWidgetDTO) {
    val app: App_DTO
    val inputValue: String

    init {
        this.app = appWidgetDTO.app
        this.inputValue = appWidgetDTO.inputValue
    }
}
