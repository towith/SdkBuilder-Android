package com.duy.compile.external.android

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.Log
import com.android.annotations.NonNull
import com.android.sdklib.build.ApkBuilderMain
import com.duy.compile.external.CompileHelper
import com.duy.compile.external.android.util.S.dirLibs
import com.duy.ide.file.FileManager
import com.duy.project.file.android.AndroidProjectFolder
import com.sun.tools.javac.main.Main
import com.willbe.builder.MainActivity
import kellinwood.security.zipsigner.ProgressEvent
import kellinwood.security.zipsigner.ZipSigner
import kellinwood.security.zipsigner.optional.CustomKeySigner
import java.io.File
import java.util.*
import javax.tools.DiagnosticCollector


class AndroidBuilder {

    fun run() {


    }

    open class SignProgress : kellinwood.security.zipsigner.ProgressListener {

        override fun onProgress(event: kellinwood.security.zipsigner.ProgressEvent) {}

    }

    companion object {
        private val TAG = "BuildTask"

        @Throws(Exception::class)
        fun build(context: Context, projectFile: AndroidProjectFolder,
                  @NonNull diagnosticCollector: DiagnosticCollector<*>) {
            AndroidBuilder.extractLibrary(projectFile)
            //create R.java
            val progressHandler = (context as MainActivity).progressHandler

            val msg0 = "Run aidl"
            println(msg0)
            val t0 = System.currentTimeMillis()
            spin(msg0, progressHandler, context)


            AndroidBuilder.runAidl(projectFile)
            val t1 = System.currentTimeMillis()
            spin("spend:${(t1 - t0) / 1000}s", progressHandler, context)
            val msg1 = "Run aapt"
            println(msg1)
            spin(msg1, progressHandler, context)
            AndroidBuilder.runAapt(context, projectFile)

            //compile java
            val msg2 = "Compile Java file"
            val t2 = System.currentTimeMillis()
            spin("spend:${(t2 - t1) / 1000}s", progressHandler, context)
            println(msg2)
            spin(msg2, progressHandler, context)
            val status = CompileHelper.compileJava(context, projectFile, diagnosticCollector)
            System.gc()
            if (status != Main.EXIT_OK) {
                println("Compile error")
                throw RuntimeException("Compile time error!")
            }

            //classes to dex
            val msg3 = "Convert classes to dex"
            val t3 = System.currentTimeMillis()
            spin("spend:${(t3 - t2) / 1000}s", progressHandler, context)
            println(msg3)
            spin(msg3, progressHandler, context)
            CompileHelper.convertToDexFormat(context, projectFile)

            //zip apk
            val msg4 = "Build apk"
            val t4 = System.currentTimeMillis()
            spin("spend:${(t4 - t3) / 1000}s", progressHandler, context)
            println(msg4)
            spin(msg4, progressHandler, context)
            AndroidBuilder.buildApk(projectFile)

            val msg5 = "Zip sign"
            val t5 = System.currentTimeMillis()
            spin("spend:${(t5 - t4) / 1000}s", progressHandler, context)
            println(msg5)
            spin(msg5, progressHandler, context)
            AndroidBuilder.zipSign(projectFile)

            val msg6 = "Zip align"
            val t6 = System.currentTimeMillis()
            spin("spend:${(t6 - t5) / 1000}s", progressHandler, context)
            println(msg6)
            spin(msg6, progressHandler, context)
            AndroidBuilder.zipAlign()

            val msg7 = "Publish apk"
            val t7 = System.currentTimeMillis()
            spin("spend:${(t7 - t6) / 1000}s", progressHandler, context)
            println(msg7)
            spin(msg7, progressHandler, context)
            AndroidBuilder.publishApk(projectFile)
        }

        private fun extractLibrary(projectFolder: AndroidProjectFolder) {
            val files = dirLibs.listFiles()
            if (files != null) {
                for (lib in files) {
                    if (lib.isFile && lib.path.endsWith(".aar")) {
                    }
                }
            }
        }

        private fun spin(msg0: String, progressHandler: Handler, context: Context) {
            val msg = Message()
            msg.obj = msg0
            progressHandler.sendMessage(msg)
            //        MainActivity activity = (MainActivity) context;
            //        activity.spin(msg0);
        }

        @Throws(Exception::class)
        private fun runAidl(projectFile: AndroidProjectFolder) {
            Log.d(TAG, "runAidl() called")

            // TODO make aidl.so
        }

        @Throws(Exception::class)
        private fun runAapt(context: Context, projectFile: AndroidProjectFolder) {
            Log.d(TAG, "runAapt() called")

            val aapt = com.duy.aapt.Aapt()
            val command = StringBuilder("aapt p -f --auto-add-overlay"
                    + " -v"  //print info

                    + " -m"  // mk package dir

                    + " -0 apk"  // ignore apk

                    //                + " --extra-packages " + new File(projectFile.getRootDir(), "libs").getPath()  //
                    + " -M " + projectFile.xmlManifest.path  //manifest file

                    + " -F " + projectFile.resourceFile.path  //output resources.ap_

                    + " -I " + FileManager.getClasspathFile(context).path  //include

                    + " -A " + projectFile.dirAssets.path  //input assets dir

                    + " -S " + projectFile.dirRes.path  //input resource dir

                    + " -J " + projectFile.classR.parent //parent file of R.java file
            )

            //test
            //        File appcompatDir = new File(Environment.getExternalStorageDirectory(), ".JavaNIDE/appcompat-v7-21.0.0");
            //        File appcompatRes = new File(appcompatDir, "res");
            //        File appcompatAsset = new File(appcompatDir, "assets");
            //        command.append(" -S ").append(appcompatRes.getPath());
            //        command.append(" -A ").append(appcompatAsset.getPath());

            val dirLibs = projectFile.getDirLibs()
            val files = dirLibs.listFiles()
            if (files != null) {
                for (lib in files) {
                    if (lib.isFile && false) {
                        if (lib.path.endsWith(".jar")) {
                            command.append(" -I ").append(lib.path)
                        } else if (lib.path.endsWith(".aar")) {
                            command.append(" -I ").append(lib.path).append(File.separator).append("res")
                        }
                    }
                }
            }
            Log.d(TAG, "runAapt command = " + command)
            val exitCode = aapt.fnExecute(command.toString())
            if (exitCode != 0) {
                throw Exception("AAPT exit($exitCode)")
            }

        }

        @Throws(Exception::class)
        private fun buildApk(projectFile: AndroidProjectFolder) {
            val args = arrayOf(projectFile.apkUnsigned.path, "-v", "-u", "-z", projectFile.resourceFile.path, "-f", projectFile.dexedClassesFile.path)
            Log.d(TAG, "buildApk args = " + Arrays.toString(args))
            ApkBuilderMain.main(args)
        }

        @Throws(Exception::class)
        private fun zipSign(projectFile: AndroidProjectFolder) {
            //        if (!appContext.getString(R.string.keystore).contentEquals(projectFile.jksEmbedded.getName())) {
            //             TODO use user defined certificate
            //        }

            // use embedded private key
            val keyStore = projectFile.keyStore
            val keystorePath = keyStore.file.path
            val keystorePw = keyStore.password
            val certAlias = keyStore.certAlias
            val certPw = keyStore.certPassword
            val signatureAlgorithm = "SHA1withRSA"

            val zipsigner = ZipSigner()
            zipsigner.addProgressListener(object : SignProgress() {
                override fun onProgress(event: ProgressEvent) {
                    super.onProgress(event)
                    println("Sign progress: " + event.percentDone)
                }
            })
            CustomKeySigner.signZip(zipsigner, keystorePath, keystorePw, certAlias,
                    certPw, signatureAlgorithm,
                    projectFile.apkUnsigned.path,
                    projectFile.apkUnaligned.path)
        }


        @Throws(Exception::class)
        private fun zipAlign() {
            //         TODO make zipalign.so
        }

        @Throws(Exception::class)
        private fun publishApk(projectFile: AndroidProjectFolder) {
            //        if (projectFile.apkRedistributable.exists()) {
            //            projectFile.apkRedistributable.delete();
            //        }
            //        Util.copy(projectFile.apkUnaligned, new FileOutputStream(projectFile.apkRedistributable));
            //
            //        projectFile.apkRedistributable.setReadable(true, false);
        }
    }

}