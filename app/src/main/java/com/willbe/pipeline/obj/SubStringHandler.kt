package com.willbe.giftapp.appPipe.obj


class SubStringHandler(widgetConfig: WidgetConfig) : Handler {
    var widgetConfig: SubStringConfig = widgetConfig as SubStringConfig


    override fun doHandle(context: Context) {
        apply(widgetConfig.inputValue, context)
    }


    fun apply(replacement: String, context: Context) {
        var targetFile = widgetConfig.targetFile
        replace(targetFile, widgetConfig.placeHolder, replacement)
    }

    private fun replace(targetFile: String, token: String, replacement: String) {
    }

}
