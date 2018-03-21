package com.willbe.builder

import android.content.Intent
import android.net.Uri
import android.os.*
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.duy.compile.BuildApkTask
import com.duy.ide.file.FileManager
import com.duy.project.file.android.AndroidProjectFolder
import com.willbe.utils.FileUtil
import java.io.File
import java.util.concurrent.ExecutionException
import javax.tools.Diagnostic

class MainActivity : AppCompatActivity() {

    var progressHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            spin(msg.obj as String)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val fab = findViewById(R.id.fab) as FloatingActionButton

        fab.setOnClickListener {
            try {
                doBuild()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //                        .setAction("Action", null).show();
        }
    }

    @Throws(Exception::class)
    private fun doBuild() {
        // prepare
        clearText()
        FileUtil.copyFileOrDir("templates")
        FileUtil.copyAssetFileToDest("android.jar",
                MyApp.getAppContext()
                        .filesDir.toString() + File.separator + FileManager.SDK_DIR + File.separator + "android.jar")

        //        File filesDir = MyApp.getAppContext().getFilesDir();

        val buildApkTask = BuildApkTask(this, object : BuildApkTask.CompileListener {
            override fun onStart() {}

            override fun onError(e: Exception?, diagnostics: List<Diagnostic<*>>) {}

            override fun onComplete(apk: File, diagnostics: List<Diagnostic<*>>) {}
        })


        val asyncTask = buildApkTask.execute(AndroidProjectFolder(
                File(
                        FileUtil.APP_DATA_PATH,
                        "templates"),
                "",
                "",
                ""))

        Thread(Runnable {
            var buildApk: File? = null
            try {
                buildApk = asyncTask.get() // blocked
                if (Build.VERSION.SDK_INT >= 24) {
                    val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                    m.invoke(null)
                }

                clearProgress()

                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.fromFile(buildApk),
                        "application/vnd.android.package-archive")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)

            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }

        }).start()

    }

    private fun clearProgress() {
        val progressBar = this.findViewById(R.id.progressBar) as ProgressBar
        progressBar.visibility = View.INVISIBLE
    }

    private fun clearText() {
        val textView = this.findViewById(R.id.spinText) as TextView
        textView.text = ""
    }

    fun spin(msg0: String) {
        //        ActivityManager activityManager = (ActivityManager) MyApp.getAppContext().getSystemService(ACTIVITY_SERVICE);
        //        ComponentName topActivity = activityManager.getRunningTasks(1).get(0).topActivity;
        val progressBar = this.findViewById(R.id.progressBar) as ProgressBar
        progressBar.visibility = View.VISIBLE
        progressBar.progress = 50
        val spinText = this.findViewById(R.id.spinText) as TextView
        spinText.text = "${spinText.text}\n${msg0}"

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }
}
