package com.duy.compile

import android.content.Context
import android.os.AsyncTask
import com.duy.compile.external.CompileHelper
import com.duy.project.file.android.AndroidProjectFolder
import java.io.File
import javax.tools.Diagnostic
import javax.tools.DiagnosticCollector

class BuildApkTask(private val context: Context, private val listener: BuildApkTask.CompileListener) : AsyncTask<AndroidProjectFolder, Any, File>() {
    private val mDiagnosticCollector: DiagnosticCollector<*>
    private var error: Exception? = null

    init {
        mDiagnosticCollector = DiagnosticCollector<Any>()
    }

    public override fun doInBackground(vararg params: AndroidProjectFolder): File? {
        val projectFile = params[0]
        if (params[0] == null) return null

        //clean
        projectFile.clean()
        try {
            return CompileHelper.buildApk(context, projectFile, mDiagnosticCollector)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun onPreExecute() {
        super.onPreExecute()
        listener.onStart()
    }

    override fun onPostExecute(result: File?) {
        super.onPostExecute(result)
        if (result == null) {
            listener.onError(error, mDiagnosticCollector.diagnostics)
        } else {
            listener.onComplete(result, mDiagnosticCollector.diagnostics)
        }
    }

    interface CompileListener {
        fun onStart()

        fun onError(e: Exception?, diagnostics: List<Diagnostic<*>>)

        fun onComplete(apk: File, diagnostics: List<Diagnostic<*>>)

    }

    companion object {
        private val TAG = "BuildApkTask"
    }
}
