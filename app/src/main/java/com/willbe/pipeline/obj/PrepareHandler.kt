package com.willbe.giftapp.appPipe.obj

import com.android.utils.FileUtils
import com.willbe.pipeline.Constant
import java.io.File
import java.io.IOException

class PrepareHandler : Handler {
    @Throws(IOException::class)
    override fun doHandle(context: Context) {
        copyWorkSpace(context)
    }

    @Throws(IOException::class)
    private fun copyWorkSpace(context: Context) {
        var outputDir = File(context.outputPath!!)
        outputDir.deleteRecursively();
        outputDir.mkdir()

        var file = File(Constant.templateRepoDir + File.separator + context.app.templatePath)
        if (file.isDirectory) {

        } else {
            throw RuntimeException("Template is not directory")
        }
        var list = file.listFiles()
        list.forEach() {
            FileUtils.copyFile(it, File(outputDir.absolutePath + File.separator + it.name))
        }
    }
}
