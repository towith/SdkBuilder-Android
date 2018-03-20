package com.duy.compile.external.android;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.android.annotations.NonNull;
import com.android.sdklib.build.ApkBuilderMain;
import com.duy.compile.external.CompileHelper;
import com.duy.ide.file.FileManager;
import com.duy.project.file.android.AndroidProjectFolder;
import com.duy.project.file.android.KeyStore;
import com.sun.tools.javac.main.Main;
import com.willbe.builder.MainActivity;
import com.willbe.builder.R;
import kellinwood.security.zipsigner.ProgressEvent;
import kellinwood.security.zipsigner.ZipSigner;
import kellinwood.security.zipsigner.optional.CustomKeySigner;

import javax.tools.DiagnosticCollector;
import java.io.File;
import java.util.Arrays;

import static com.duy.compile.external.android.util.S.dirLibs;


public class AndroidBuilder {
    private static final String TAG = "BuildTask";

    public static void build(Context context, AndroidProjectFolder projectFile,
                             @NonNull DiagnosticCollector diagnosticCollector) throws Exception {
        AndroidBuilder.extractLibrary(projectFile);
        //create R.java
        Handler progressHandler = ((MainActivity) context).progressHandler;

        String msg0 = "Run aidl";
        System.out.println(msg0);
        long t0 = System.currentTimeMillis();
        spin(msg0, progressHandler, context);


        AndroidBuilder.runAidl(projectFile);
        long t1 = System.currentTimeMillis();
        String msg1 = "Run aapt";
        System.out.println(msg1);
        spin(msg1, progressHandler, context);
        AndroidBuilder.runAapt(context, projectFile);

        //compile java
        String msg2 = "Compile Java file";
        System.out.println(msg2);
        spin(msg2, progressHandler, context);
        int status = CompileHelper.compileJava(context, projectFile, diagnosticCollector);
        System.gc();
        if (status != Main.EXIT_OK) {
            System.out.println("Compile error");
            throw new RuntimeException("Compile time error!");
        }

        //classes to dex
        String msg3 = "Convert classes to dex";
        System.out.println(msg3);
        spin(msg3, progressHandler, context);
        CompileHelper.convertToDexFormat(context, projectFile);

        //zip apk
        String msg4 = "Build apk";
        System.out.println(msg4);
        spin(msg4, progressHandler, context);
        AndroidBuilder.buildApk(projectFile);

        String msg5 = "Zip sign";
        System.out.println(msg5);
        spin(msg5, progressHandler, context);
        AndroidBuilder.zipSign(projectFile);

        String msg6 = "Zip align";
        System.out.println(msg6);
        spin(msg6, progressHandler, context);
        AndroidBuilder.zipAlign();

        String msg7 = "Publish apk";
        System.out.println(msg7);
        spin(msg7, progressHandler, context);
        AndroidBuilder.publishApk(projectFile);
        Activity activity = (Activity) context;
        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

    }

    private static void extractLibrary(AndroidProjectFolder projectFolder) {
        File[] files = dirLibs.listFiles();
        if (files != null) {
            for (File lib : files) {
                if (lib.isFile() && lib.getPath().endsWith(".aar")) {
                }
            }
        }
    }

    private static void spin(final String msg0, final Handler progressHandler, Context context) {
        Message msg = new Message();
        msg.obj = msg0;
        progressHandler.sendMessage(msg);
//        MainActivity activity = (MainActivity) context;
//        activity.spin(msg0);
    }

    private static void runAidl(AndroidProjectFolder projectFile) throws Exception {
        Log.d(TAG, "runAidl() called");

        // TODO make aidl.so
    }

    private static void runAapt(Context context, AndroidProjectFolder projectFile) throws Exception {
        Log.d(TAG, "runAapt() called");

        com.duy.aapt.Aapt aapt = new com.duy.aapt.Aapt();
        StringBuilder command = new StringBuilder("aapt p -f --auto-add-overlay"
                + " -v"  //print info
                + " -m"  // mk package dir
                + " -0 apk"  // ignore apk
//                + " --extra-packages " + new File(projectFile.getRootDir(), "libs").getPath()  //
                + " -M " + projectFile.getXmlManifest().getPath()  //manifest file
                + " -F " + projectFile.getResourceFile().getPath()  //output resources.ap_
                + " -I " + FileManager.getClasspathFile(context).getPath()  //include
                + " -A " + projectFile.getDirAssets().getPath()  //input assets dir
                + " -S " + projectFile.getDirRes().getPath()  //input resource dir
                + " -J " + projectFile.getClassR().getParent() //parent file of R.java file
        );

        //test
//        File appcompatDir = new File(Environment.getExternalStorageDirectory(), ".JavaNIDE/appcompat-v7-21.0.0");
//        File appcompatRes = new File(appcompatDir, "res");
//        File appcompatAsset = new File(appcompatDir, "assets");
//        command.append(" -S ").append(appcompatRes.getPath());
//        command.append(" -A ").append(appcompatAsset.getPath());

        File dirLibs = projectFile.getDirLibs();
        File[] files = dirLibs.listFiles();
        if (files != null) {
            for (File lib : files) {
                if (lib.isFile() && false) {
                    if (lib.getPath().endsWith(".jar")) {
                        command.append(" -I ").append(lib.getPath());
                    }
                    else if (lib.getPath().endsWith(".aar")) {
                        command.append(" -I ").append(lib.getPath()).append(File.separator).append("res");
                    }
                }
            }
        }
        Log.d(TAG, "runAapt command = " + command);
        int exitCode = aapt.fnExecute(command.toString());
        if (exitCode != 0) {
            throw new Exception("AAPT exit(" + exitCode + ")");
        }

    }

    private static void buildApk(AndroidProjectFolder projectFile) throws Exception {
        String[] args = {
                projectFile.getApkUnsigned().getPath(),
                "-v",
                "-u",
                "-z", projectFile.getResourceFile().getPath(),
                "-f", projectFile.getDexedClassesFile().getPath()
        };
        Log.d(TAG, "buildApk args = " + Arrays.toString(args));
        ApkBuilderMain.main(args);
    }

    private static void zipSign(AndroidProjectFolder projectFile) throws Exception {
//        if (!appContext.getString(R.string.keystore).contentEquals(projectFile.jksEmbedded.getName())) {
//             TODO use user defined certificate
//        }

        // use embedded private key
        KeyStore keyStore = projectFile.getKeyStore();
        String keystorePath = keyStore.getFile().getPath();
        char[] keystorePw = keyStore.getPassword();
        String certAlias = keyStore.getCertAlias();
        char[] certPw = keyStore.getCertPassword();
        String signatureAlgorithm = "SHA1withRSA";

        ZipSigner zipsigner = new ZipSigner();
        zipsigner.addProgressListener(new SignProgress() {
            @Override
            public void onProgress(ProgressEvent event) {
                super.onProgress(event);
                System.out.println("Sign progress: " + event.getPercentDone());
            }
        });
        CustomKeySigner.signZip(zipsigner, keystorePath, keystorePw, certAlias,
                certPw, signatureAlgorithm,
                projectFile.getApkUnsigned().getPath(),
                projectFile.getApkUnaligned().getPath());
    }


    private static void zipAlign() throws Exception {
//         TODO make zipalign.so
    }

    private static void publishApk(AndroidProjectFolder projectFile) throws Exception {
//        if (projectFile.apkRedistributable.exists()) {
//            projectFile.apkRedistributable.delete();
//        }
//        Util.copy(projectFile.apkUnaligned, new FileOutputStream(projectFile.apkRedistributable));
//
//        projectFile.apkRedistributable.setReadable(true, false);
    }

    public void run() {


    }

    public static class SignProgress implements kellinwood.security.zipsigner.ProgressListener {

        public void onProgress(kellinwood.security.zipsigner.ProgressEvent event) {
        }

    }

}